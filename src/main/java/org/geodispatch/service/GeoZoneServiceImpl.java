package org.geodispatch.service;

import jakarta.ejb.Stateless;

import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.entity.Vehicle;

import java.util.List;

/**
 * Implementation of {@link GeoZoneService}.
 */
@Stateless
public class GeoZoneServiceImpl extends CrudServiceImpl<GeoZone> implements GeoZoneService {

    public GeoZoneServiceImpl() {
        super(GeoZone.class);
    }

    @Override
    public boolean contains(Long zoneId, double lat, double lon) {
        GeoZone zone = findById(zoneId);
        if (zone == null) return false;
        return distanceMeters(zone.getLatitude(), zone.getLongitude(), lat, lon)
                <= zone.getRadiusMeters();
    }

    @Override
    public List<Vehicle> findVehiclesInsideZone(Long zoneId) {

        GeoZone zone = findById(zoneId);
        if (zone == null) return List.of();

        // получаем ВСЕ последние пинги
        List<TrackerPing> latestPings = em.createQuery(
                "SELECT p FROM TrackerPing p WHERE p.timestamp = (" +
                        "   SELECT MAX(p2.timestamp) FROM TrackerPing p2 WHERE p2.vehicle.id = p.vehicle.id" +
                        ")",
                TrackerPing.class
        ).getResultList();

        // фильтруем по расстоянию
        return latestPings.stream()
                .filter(p -> p.getVehicle() != null)
                .filter(p -> distanceMeters(
                        zone.getLatitude(),
                        zone.getLongitude(),
                        p.getLatitude(),
                        p.getLongitude()
                ) <= zone.getRadiusMeters())
                .map(TrackerPing::getVehicle)
                .distinct()
                .toList();
    }

    /**
     * Вычисление расстояния между двумя точками в метрах (haversine).
     */
    private static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371000.0; // радиус Земли

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }

}

