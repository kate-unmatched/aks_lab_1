package org.geodispatch.service.ping;

import jakarta.ejb.Stateless;

import org.geodispatch.entity.TrackerPing;
import org.geodispatch.service.base.CrudServiceImpl;

import java.util.List;

@Stateless
public class TrackerPingServiceImpl extends CrudServiceImpl<TrackerPing> implements TrackerPingService {

    public TrackerPingServiceImpl() {
        super(TrackerPing.class);
    }

    @Override
    public List<TrackerPing> findByVehicle(Long vehicleId) {
        return em.createQuery(
                "SELECT p FROM TrackerPing p WHERE p.vehicle.id = :vehicleId ORDER BY p.timestamp DESC",
                TrackerPing.class
        )
        .setParameter("vehicleId", vehicleId)
        .getResultList();
    }
}
