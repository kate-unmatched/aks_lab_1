package org.geodispatch.services;

import jakarta.ejb.Stateless;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.services.base.GenericCrudServiceImpl;

import java.util.List;

@Stateless
public class VehicleServiceImpl extends GenericCrudServiceImpl<Vehicle> implements VehicleService {

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
