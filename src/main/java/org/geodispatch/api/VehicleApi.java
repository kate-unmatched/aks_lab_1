package org.geodispatch.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.Vehicle;

@Path("/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface VehicleApi {

    @GET
    Response getAll();

    @GET
    @Path("/active")
    Response getActive();

    @GET
    @Path("/{id}")
    Response getById(@PathParam("id") Long id);

    @POST
    Response create(Vehicle vehicle);

    @PUT
    @Path("/{id}")
    Response update(@PathParam("id") Long id, Vehicle vehicle);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);
}
