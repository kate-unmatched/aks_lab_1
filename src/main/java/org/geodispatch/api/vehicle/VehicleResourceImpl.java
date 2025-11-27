package org.geodispatch.api.vehicle;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.AbstractCrudResource;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.service.ping.TrackerPingService;
import org.geodispatch.service.vehicle.VehicleService;
import org.geodispatch.service.base.CrudService;


import java.util.Comparator;
import java.util.List;

public class VehicleResourceImpl extends AbstractCrudResource<Vehicle> implements VehicleResource {

    @EJB
    private VehicleService vehicleService;
    @EJB
    private TrackerPingService pingService;

    @Override
    protected CrudService<Vehicle> getService() {
        return vehicleService;
    }

    @Override
    protected void applyUpdates(Vehicle existing, Vehicle newData) {
        existing.setRegistrationPlate(newData.getRegistrationPlate());
        existing.setModel(newData.getModel());
        existing.setManufacturer(newData.getManufacturer());
    }

    @Override
    public List<Vehicle> getActiveVehicles() {
        return vehicleService.findActiveVehicles();
    }

    @Override
    public Response getLastPing(Long vehicleId) {
        List<TrackerPing> pings = pingService.findByVehicle(vehicleId);

        if (pings.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No pings for this vehicle")
                    .build();
        }

        TrackerPing last = pings.stream()
                .max(Comparator.comparing(TrackerPing::getTimestamp))
                .orElse(null);

        return Response.ok(last).build();
    }
}
