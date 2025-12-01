package org.geodispatch.service;

import jakarta.ejb.Stateless;

import org.geodispatch.entity.TrackerPing;

import java.util.List;

@Stateless
public class TrackerPingServiceImpl extends CrudServiceImpl<TrackerPing> implements TrackerPingService {

    public TrackerPingServiceImpl() {
        super(TrackerPing.class);
    }

    @Override
    public List<TrackerPing> findByVehicle(Long vehicleId) {
        return em.createQuery(
                        "SELECT p FROM TrackerPing p " +
                                "WHERE p.vehicle.id = :vehicleId " +
                                "ORDER BY p.timestamp DESC",
                        TrackerPing.class
                )
                .setParameter("vehicleId", vehicleId)
                .getResultList();
    }

    @Override
    public TrackerPing findLastPing(Long vehicleId) {
        List<TrackerPing> list = em.createQuery(
                        "SELECT p FROM TrackerPing p " +
                                "WHERE p.vehicle.id = :vehicleId " +
                                "ORDER BY p.timestamp DESC",
                        TrackerPing.class
                )
                .setParameter("vehicleId", vehicleId)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TrackerPing> findInsideZone(Long zoneId) {
        return em.createQuery(
                        "SELECT p FROM TrackerPing p " +
                                "JOIN FETCH p.vehicle v " +
                                "JOIN FETCH ZoneVisit zv ON zv.vehicle.id = v.id " +
                                "WHERE zv.zone.id = :zoneId " +
                                "ORDER BY p.timestamp DESC",
                        TrackerPing.class
                )
                .setParameter("zoneId", zoneId)
                .getResultList();
    }
}
