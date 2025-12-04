package org.geodispatch.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.geodispatch.api.VehicleApi;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.VehicleService;

import java.net.URI;
import java.util.List;

public class VehicleApiImpl implements VehicleApi {

    @Inject
    private VehicleService service;

    @Override
    public Response getAll() {
        List<Vehicle> vehicles = service.findAll();
        return Response.ok(vehicles).build();
    }

    @Override
    public Response getActive() {
        List<Vehicle> vehicles = service.findActiveVehicles();
        return Response.ok(vehicles).build();
    }

    @Override
    public Response getById(Long id) {
        Vehicle v = service.findById(id);

        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Vehicle not found")
                    .build();
        }

        return Response.ok(v).build();
    }

    @Override
    public Response create(Vehicle vehicle) {
        Vehicle created = service.create(vehicle);

        URI location = UriBuilder.fromPath("/api/v1/vehicles/{id}")
                .build(created.getId());

        return Response.created(location)
                .entity(created)
                .build();
    }

    @Override
    public Response update(Long id, Vehicle vehicle) {
        vehicle.setId(id);
        Vehicle updated = service.update(vehicle);
        return Response.ok(updated).build();
    }

    @Override
    public Response delete(Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
