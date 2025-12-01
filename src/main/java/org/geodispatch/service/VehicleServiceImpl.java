package org.geodispatch.service;

import jakarta.ejb.Stateless;
import org.geodispatch.entity.Vehicle;

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
