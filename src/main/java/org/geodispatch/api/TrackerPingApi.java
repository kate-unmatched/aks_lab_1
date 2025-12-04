package org.geodispatch.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.TrackerPing;

@Path("/pings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TrackerPingApi {

    @GET
    Response findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") Long id);

    @GET
    @Path("/vehicle/{vehicleId}")
    Response findByVehicle(@PathParam("vehicleId") Long vehicleId);

    @GET
    @Path("/vehicle/{vehicleId}/last")
    Response lastPing(@PathParam("vehicleId") Long vehicleId);

    @POST
    Response create(TrackerPing ping);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);
}
