package org.geodispatch.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.Vehicle;

@Path("/zones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GeoZoneApi {

    @GET
    Response findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") Long id);

    @POST
    Response create(GeoZone zone);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);

    // Проверить попадание точки в радиус зоны
    @GET
    @Path("/{id}/contains")
    Response contains(
            @PathParam("id") Long id,
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon
    );

    // Машины, находящиеся сейчас в зоне (по последнему пингу)
    @GET
    @Path("/{id}/vehicles")
    Response vehiclesInside(@PathParam("id") Long id);

    @GET
    @Path("/stats")
    Response getAllZonesStats();
}
