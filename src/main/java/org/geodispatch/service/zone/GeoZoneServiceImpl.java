package org.geodispatch.service.zone;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import org.geodispatch.entity.GeoZone;
import org.geodispatch.service.base.CrudServiceImpl;

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
    public List<GeoZone> findContainingPoint(double latitude, double longitude) {
        // Получаем ВСЕ зоны (оптимизация не критична)
        List<GeoZone> zones = findAll();

        return zones.stream()
                .filter(z -> isInside(z, latitude, longitude))
                .toList();
    }

    private boolean isInside(GeoZone zone, double lat, double lon) {
        double dLat = Math.toRadians(lat - zone.getLatitude());
        double dLon = Math.toRadians(lon - zone.getLongitude());

        double rLat1 = Math.toRadians(zone.getLatitude());
        double rLat2 = Math.toRadians(lat);

        // Формула гаверсинусов
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(rLat1) * Math.cos(rLat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double EARTH_RADIUS_M = 6371000.0;
        double distance = EARTH_RADIUS_M * c;

        return distance <= zone.getRadiusMeters();
    }

    @Override
    public GeoZone findByName(String name) {
        TypedQuery<GeoZone> q = em.createQuery(
                "SELECT z FROM GeoZone z WHERE z.name = :n", GeoZone.class
        );
        q.setParameter("n", name);

        List<GeoZone> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
