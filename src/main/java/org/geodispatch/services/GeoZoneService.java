package org.geodispatch.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.geodispatch.entity.GeoZone;

import java.util.*;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GeoZoneService {

    @PersistenceContext(unitName = "geoPU")
    private EntityManager em;

    /** Создать геозону (проверит уникальность name). */
    public GeoZone create(String name, double latitude, double longitude, double radiusMeters) {
        if (existsByName(name)) {
            throw new IllegalArgumentException("GeoZone with name '" + name + "' already exists");
        }
        GeoZone z = new GeoZone();
        z.setName(name.trim());
        z.setLatitude(latitude);
        z.setLongitude(longitude);
        z.setRadiusMeters(requirePositive(radiusMeters, "radiusMeters"));
        em.persist(z);
        return z;
    }

    /** Обновить основные поля зоны. */
    public GeoZone update(long id, String name, Double latitude, Double longitude, Double radiusMeters) {
        GeoZone z = getOrThrow(id);
        if (name != null && !name.trim().isEmpty() && !name.equals(z.getName())) {
            if (existsByName(name)) throw new IllegalArgumentException("GeoZone name already used: " + name);
            z.setName(name.trim());
        }
        if (latitude != null)  z.setLatitude(latitude);
        if (longitude != null) z.setLongitude(longitude);
        if (radiusMeters != null) z.setRadiusMeters(requirePositive(radiusMeters, "radiusMeters"));
        return z;
    }

    /** Удалить зону. Бросит IllegalStateException если есть ссылки (FK). */
    public void delete(long id) {
        GeoZone z = getOrThrow(id);
        em.remove(z); // при наличии ссылок DB даст FK violation → завернётся в PersistenceException
    }

    public Optional<GeoZone> findById(long id) {
        return Optional.ofNullable(em.find(GeoZone.class, id));
    }

    public boolean existsByName(String name) {
        Long cnt = em.createQuery("select count(z) from GeoZone z where lower(z.name)=lower(:n)", Long.class)
                .setParameter("n", name.trim())
                .getSingleResult();
        return cnt != 0;
    }

    public Optional<GeoZone> findByName(String name) {
        List<GeoZone> list = em.createQuery("select z from GeoZone z where lower(z.name)=lower(:n)", GeoZone.class)
                .setParameter("n", name.trim())
                .setMaxResults(1)
                .getResultList();
        return list.stream().findFirst();
    }

    /** Простая пагинация. */
    public List<GeoZone> list(int offset, int limit) {
        TypedQuery<GeoZone> q = em.createQuery("select z from GeoZone z order by z.name asc", GeoZone.class);
        if (offset > 0) q.setFirstResult(offset);
        if (limit  > 0) q.setMaxResults(limit);
        return q.getResultList();
    }

    /** Количество всех зон (для UI-пагинации). */
    public long countAll() {
        return em.createQuery("select count(z) from GeoZone z", Long.class).getSingleResult();
    }

    /** Обновить только радиус. */
    public GeoZone updateRadius(long id, double newRadiusMeters) {
        GeoZone z = getOrThrow(id);
        z.setRadiusMeters(requirePositive(newRadiusMeters, "radiusMeters"));
        return z;
    }

    /**
     * Найти зоны, которые содержат точку (lat, lon).
     * Делаем быструю предфильтрацию по bounding box, затем точную проверку Haversine.
     */
    public List<GeoZone> findZonesContaining(double latitude, double longitude) {
        // 1) Грубая выборка по окрестности в 1 км, чтобы не тянуть всё (настраиваемо)
        double approxKm = 1.0;
        double latDelta = approxKm / 111.0; // ~111 км на 1° широты
        double lonDelta = approxKm / (111.0 * Math.cos(Math.toRadians(latitude)));

        List<GeoZone> nearby = em.createQuery(
                "select z from GeoZone z " +
                        "where z.latitude  between :latMin and :latMax " +
                        "and   z.longitude between :lonMin and :lonMax",
                GeoZone.class)
                .setParameter("latMin", latitude - latDelta)
                .setParameter("latMax", latitude + latDelta)
                .setParameter("lonMin", longitude - lonDelta)
                .setParameter("lonMax", longitude + lonDelta)
                .getResultList();

        // 2) Точная проверка расстоянием
        return nearby.stream()
                .filter(z -> distanceMeters(z.getLatitude(), z.getLongitude(), latitude, longitude) <= z.getRadiusMeters())
                .sorted(Comparator.comparing(GeoZone::getName))
                .collect(Collectors.toList());
    }

    private GeoZone getOrThrow(long id) {
        GeoZone z = em.find(GeoZone.class, id);
        if (z == null) throw new NoSuchElementException("GeoZone not found: id=" + id);
        return z;
    }

    private static double requirePositive(double v, String name) {
        if (v <= 0) throw new IllegalArgumentException(name + " must be > 0");
        return v;
    }

    /** Haversine в метрах. */
    public static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                + Math.sin(dLon/2)*Math.sin(dLon/2) * 1.0
                - Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2));

        // Классическая формула
        double aa = Math.sin(dLat/2)*Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1-aa));
        return R * c;
    }

    /**
     * Адаптивно подстраивает радиус зоны по истории пингов:
     * вычисляет среднее расстояние до точки по последним ping'ам машины,
     * отбрасывает выбросы и обновляет радиус с безопасным коэффициентом.
     */
    public GeoZone retuneRadius(long zoneId,
                                int lookbackDays,
                                double quantile,
                                double safetyMarginMeters,
                                double minRadiusMeters,
                                double maxRadiusMeters,
                                double emaAlpha) {

        GeoZone zone = getOrThrow(zoneId);

        // 1️⃣ Берём пинги, которые попадают в окрестность зоны
        double lat = zone.getLatitude();
        double lon = zone.getLongitude();
        double km = (zone.getRadiusMeters() + 50.0) / 1000.0; // зона + запас 50 м
        double latDelta = km / 111.0;
        double lonDelta = km / (111.0 * Math.cos(Math.toRadians(lat)));

        java.time.Instant since = java.time.Instant.now().minus(java.time.Duration.ofDays(lookbackDays));

        List<Double> distances = em.createQuery("""
            select sqrt(
                pow(111320 * (tp.latitude - :lat), 2) +
                pow(111320 * cos(radians(:lat)) * (tp.longitude - :lon), 2)
            )
            from TrackerPing tp
            where tp.timestamp >= :since
              and tp.latitude  between :latMin and :latMax
              and tp.longitude between :lonMin and :lonMax
            """, Double.class)
                .setParameter("lat", lat)
                .setParameter("lon", lon)
                .setParameter("since", since)
                .setParameter("latMin", lat - latDelta)
                .setParameter("latMax", lat + latDelta)
                .setParameter("lonMin", lon - lonDelta)
                .setParameter("lonMax", lon + lonDelta)
                .getResultList();

        if (distances.isEmpty()) return zone;

        // Отсекаем выбросы по грубой медиане
        distances.sort(Double::compareTo);
        double median = distances.get(distances.size() / 2);
        double mad = distances.stream().mapToDouble(d -> Math.abs(d - median)).average().orElse(0);
        double cutoff = median + 3 * mad;
        List<Double> filtered = distances.stream().filter(d -> d <= cutoff).toList();
        if (filtered.isEmpty()) filtered = distances;

        // Берём квантиль (например, 0.9)
        double newRadius = percentile(filtered, quantile) + safetyMarginMeters;

        // Ограничения min/max и экспоненциальное сглаживание
        newRadius = Math.max(minRadiusMeters, Math.min(maxRadiusMeters, newRadius));
        double smoothed = zone.getRadiusMeters() + emaAlpha * (newRadius - zone.getRadiusMeters());

        zone.setRadiusMeters(smoothed);
        return zone;
    }

    private static double percentile(List<Double> sorted, double q) {
        if (sorted.isEmpty()) return 0;
        int n = sorted.size();
        int idx = (int) Math.floor(q * (n - 1));
        if (idx < 0) idx = 0;
        if (idx >= n) idx = n - 1;
        return sorted.get(idx);
    }
}
