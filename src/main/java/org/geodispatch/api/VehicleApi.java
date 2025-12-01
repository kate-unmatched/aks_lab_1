package org.geodispatch.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.VehicleService;

import java.net.URI;
import java.util.List;

@Path("/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleApi {

    @Inject
    private VehicleService service;

   @GET
    public Response getAll() {
        List<Vehicle> vehicles = service.findAll();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/active")
    public Response getActive() {
        List<Vehicle> vehicles = service.findActiveVehicles();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Vehicle v = service.findById(id);

        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Vehicle not found")
                    .build();
        }

        return Response.ok(v).build();
    }

    @POST
    public Response create(Vehicle vehicle) {
        Vehicle created = service.create(vehicle);

        return Response
                .created(URI.create("/api/v1/vehicles/" + created.getId()))
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Vehicle v) {
        v.setId(id);
        Vehicle updated = service.update(v);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
