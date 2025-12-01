package org.geodispatch.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.service.TrackerPingService;

import java.util.List;

@Path("/pings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrackerPingApi {

    @EJB
    private TrackerPingService service;

    @GET
    public Response findAll() {
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        TrackerPing ping = service.findById(id);
        return ping != null ? Response.ok(ping).build()
                            : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/vehicle/{vehicleId}")
    public Response findByVehicle(@PathParam("vehicleId") Long vehicleId) {
        List<TrackerPing> list = service.findByVehicle(vehicleId);
        return Response.ok(list).build();
    }

    @GET
    @Path("/vehicle/{vehicleId}/last")
    public Response lastPing(@PathParam("vehicleId") Long vehicleId) {
        TrackerPing ping = service.findLastPing(vehicleId);
        return ping != null ? Response.ok(ping).build()
                            : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response create(TrackerPing ping) {
        TrackerPing created = service.create(ping);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
