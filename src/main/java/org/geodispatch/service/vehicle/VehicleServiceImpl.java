package org.geodispatch.service.vehicle;

import jakarta.ejb.Stateless;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.base.CrudServiceImpl;

import java.util.List;

@Stateless
public class VehicleServiceImpl extends CrudServiceImpl<Vehicle> implements VehicleService {

    public VehicleServiceImpl() {
        super(Vehicle.class);
    }

    @Override
    public List<Vehicle> findActiveVehicles() {
        return em.createQuery(
            "SELECT DISTINCT p.vehicle FROM TrackerPing p WHERE p.speedKmh > 0",
            Vehicle.class
        ).getResultList();
    }
}
