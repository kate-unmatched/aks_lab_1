package org.geodispatch.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.GeoZoneService;

import java.util.List;

@Path("/zones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeoZoneApi {

    @EJB
    private GeoZoneService service;

    @GET
    public Response findAll() {
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        GeoZone zone = service.findById(id);
        return zone != null
                ? Response.ok(zone).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response create(GeoZone zone) {
        GeoZone created = service.create(zone);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    // Проверить попадание точки в радиус зоны
    @GET
    @Path("/{id}/contains")
    public Response contains(
            @PathParam("id") Long id,
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon
    ) {
        if (lat == null || lon == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("lat and lon are required")
                    .build();

        boolean inside = service.contains(id, lat, lon);
        return Response.ok(inside).build();
    }

    // Машины, находящиеся сейчас в зоне (по последнему пингу)
    @GET
    @Path("/{id}/vehicles")
    public Response vehiclesInside(@PathParam("id") Long id) {
        List<Vehicle> vehicles = service.findVehiclesInsideZone(id);
        return Response.ok(vehicles).build();
    }
}
