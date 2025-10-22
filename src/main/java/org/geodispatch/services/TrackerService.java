package org.geodispatch.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.JobOrder;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.entity.Vehicle;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrackerService {

    @PersistenceContext(unitName = "geoPU")
    private EntityManager em;

    @EJB private GeoZoneService geoZoneService;
    @EJB private VisitService visitService;
    @EJB private JobOrderService jobOrderService;

    /** Сохранить пинг и проверить попадание в зону (+ бизнес-правило по активному наряду). */
    public TrackerPing ingestPing(long vehicleId, Instant timestamp, double latitude, double longitude,
                                  Double speedKmh, Double headingDegrees) {
        Vehicle v = em.getReference(Vehicle.class, vehicleId);

        TrackerPing tp = new TrackerPing();
        tp.setVehicle(v);
        tp.setTimestamp(timestamp);
        tp.setLatitude(latitude);
        tp.setLongitude(longitude);
        tp.setSpeedKmh(speedKmh);
        tp.setHeadingDegrees(headingDegrees);
        em.persist(tp);

        // Найдём активный наряд
        Optional<JobOrder> active = jobOrderService.findActiveForVehicle(vehicleId);

        // Если есть запланированная зона — проверим только её; иначе — проверим все подходящие
        if (active.isPresent() && active.get().getPlannedZone() != null) {
            GeoZone target = active.get().getPlannedZone();
            if (isInside(target, latitude, longitude)) {
                // фиксируем визит и завершаем наряд
                visitService.confirmVisit(vehicleId, target.getId(), active.get().getId(),
                        timestamp, timestamp.plusSeconds(1));
                jobOrderService.complete(active.get().getId());
            }
        } else {
            // без активного наряда: просто найдём все зоны, куда попал пинг, и зафиксируем визит без orderId
            List<GeoZone> zones = geoZoneService.findZonesContaining(latitude, longitude);
            for (GeoZone z : zones) {
                visitService.confirmVisit(vehicleId, z.getId(), null, timestamp, timestamp);
            }
        }
        return tp;
    }

    public static boolean isInside(GeoZone z, double lat, double lon) {
        return distanceMeters(z.getLatitude(), z.getLongitude(), lat, lon) <= z.getRadiusMeters();
    }

    /** Haversine в метрах. */
    public static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
