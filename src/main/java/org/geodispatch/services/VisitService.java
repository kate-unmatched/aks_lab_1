package org.geodispatch.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.JobOrder;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.entity.ZoneVisit;

import java.time.Instant;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VisitService {

    @PersistenceContext(unitName = "geoPU")
    private EntityManager em;

    /** Создаёт подтверждённый визит, если такого ещё не было (idempotent по vehicle+zone+order в заданном интервале). */
    public ZoneVisit confirmVisit(long vehicleId, long zoneId, Long orderId, Instant enteredAt, Instant leftAt) {
        Vehicle v = em.getReference(Vehicle.class, vehicleId);
        GeoZone z = em.getReference(GeoZone.class, zoneId);
        JobOrder o = (orderId != null) ? em.getReference(JobOrder.class, orderId) : null;

        Long already = em.createQuery("""
                select count(vs) from ZoneVisit vs
                where vs.vehicle = :v and vs.zone = :z and vs.jobOrder = :o and vs.confirmed = true
                  and vs.enteredAt <= :left and coalesce(vs.leftAt, vs.enteredAt) >= :entered
                """, Long.class)
            .setParameter("v", v)
            .setParameter("z", z)
            .setParameter("o", o)
            .setParameter("entered", enteredAt)
            .setParameter("left", leftAt != null ? leftAt : enteredAt)
            .getSingleResult();

        if (already > 0) {
            // возвращаем любой имеющийся визит
            return em.createQuery("""
                    select vs from ZoneVisit vs
                    where vs.vehicle = :v and vs.zone = :z and vs.jobOrder = :o and vs.confirmed = true
                    order by vs.enteredAt desc
                    """, ZoneVisit.class)
                .setParameter("v", v).setParameter("z", z).setParameter("o", o)
                .setMaxResults(1).getSingleResult();
        }

        ZoneVisit visit = new ZoneVisit();
        visit.setVehicle(v);
        visit.setZone(z);
        visit.setJobOrder(o);
        visit.setEnteredAt(enteredAt);
        visit.setLeftAt(leftAt);
        visit.setConfirmed(true);
        em.persist(visit);
        return visit;
    }

    public List<ZoneVisit> listForVehicle(long vehicleId, int offset, int limit) {
        var q = em.createQuery("""
                select vs from ZoneVisit vs
                where vs.vehicle.id = :vid
                order by vs.enteredAt desc
                """, ZoneVisit.class)
            .setParameter("vid", vehicleId);
        if (offset > 0) q.setFirstResult(offset);
        if (limit > 0) q.setMaxResults(limit);
        return q.getResultList();
    }

    public List<ZoneVisit> listForZone(long zoneId, int offset, int limit) {
        var q = em.createQuery("""
                select vs from ZoneVisit vs
                where vs.zone.id = :zid
                order by vs.enteredAt desc
                """, ZoneVisit.class)
            .setParameter("zid", zoneId);
        if (offset > 0) q.setFirstResult(offset);
        if (limit > 0) q.setMaxResults(limit);
        return q.getResultList();
    }

    /**
     * Подтверждает визит в зону по "времени пребывания" (dwell-time).
     * Если суммарное время нахождения >= minDwellSec — создаёт подтверждённый визит.
     * Возвращает true, если визит подтверждён, иначе false.
     */
    public boolean confirmVisitByDwell(long zoneId,
                                       long vehicleId,
                                       Instant from,
                                       Instant to,
                                       int minDwellSec,
                                       int gapToleranceSec,
                                       Double maxSpeedKmh) {

        GeoZone zone = em.getReference(GeoZone.class, zoneId);
        Vehicle vehicle = em.getReference(Vehicle.class, vehicleId);

        // Уже подтверждённый визит?
        Long already = em.createQuery("""
                select count(vs) from ZoneVisit vs
                where vs.zone = :z and vs.vehicle = :v and vs.confirmed = true
                  and vs.enteredAt <= :to and coalesce(vs.leftAt, vs.enteredAt) >= :from
                """, Long.class)
                .setParameter("z", zone)
                .setParameter("v", vehicle)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        if (already > 0) return true;

        // Извлекаем пинги по машине
        List<Object[]> rows = em.createQuery("""
                select tp.timestamp, tp.latitude, tp.longitude, tp.speedKmh
                from TrackerPing tp
                where tp.vehicle = :v and tp.timestamp between :from and :to
                order by tp.timestamp
                """, Object[].class)
                .setParameter("v", vehicle)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();

        if (rows.isEmpty()) return false;

        // Подсчёт времени пребывания внутри зоны
        long dwell = 0;
        Instant segStart = null;
        Instant prevTs = null;
        boolean insidePrev = false;

        for (Object[] r : rows) {
            Instant ts = (Instant) r[0];
            double lat = ((Number) r[1]).doubleValue();
            double lon = ((Number) r[2]).doubleValue();
            Double speed = r[3] == null ? null : ((Number) r[3]).doubleValue();

            // фильтр "спуфинга" по скорости
            if (maxSpeedKmh != null && speed != null && speed > maxSpeedKmh) {
                insidePrev = false;
                prevTs = ts;
                continue;
            }

            boolean inside = isInside(zone, lat, lon);

            if (inside && !insidePrev) {
                segStart = ts; // вошли
            } else if (!inside && insidePrev) {
                long gap = prevTs == null ? 0 : java.time.Duration.between(prevTs, ts).getSeconds();
                if (gap > gapToleranceSec && segStart != null) {
                    dwell += java.time.Duration.between(segStart, prevTs).getSeconds();
                    segStart = null;
                }
            }

            insidePrev = inside;
            prevTs = ts;
        }

        if (insidePrev && segStart != null && prevTs != null)
            dwell += java.time.Duration.between(segStart, prevTs).getSeconds();

        if (dwell >= minDwellSec) {
            ZoneVisit visit = new ZoneVisit();
            visit.setZone(zone);
            visit.setVehicle(vehicle);
            visit.setEnteredAt(from);
            visit.setLeftAt(to);
            visit.setConfirmed(true);
            em.persist(visit);
            return true;
        }

        return false;
    }

    private static boolean isInside(GeoZone z, double lat, double lon) {
        return distanceMeters(z.getLatitude(), z.getLongitude(), lat, lon) <= z.getRadiusMeters();
    }

    private static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}
