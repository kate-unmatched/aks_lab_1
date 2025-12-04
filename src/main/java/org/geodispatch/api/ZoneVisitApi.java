package org.geodispatch.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.dtos.ZoneVisitUpdateRequestDTO;
import org.geodispatch.entity.ZoneVisit;

@Path("/zone-visits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ZoneVisitApi {

    @GET
    Response findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") Long id);

    @GET
    @Path("/zone/{zoneId}")
    Response findByZone(@PathParam("zoneId") Long zoneId);

    @GET
    @Path("/vehicle/{vehicleId}")
    Response findByVehicle(@PathParam("vehicleId") Long vehicleId);

    @PUT
    @Path("/{id}")
    Response update(@PathParam("id") Long id, ZoneVisitUpdateRequestDTO dto);

    @POST
    Response create(ZoneVisit visit);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);

    @GET
    @Path("/vehicle/{vehicleId}/last")
    Response findLastByVehicle(@PathParam("vehicleId") Long vehicleId);

    @GET
    @Path("/zone/{zoneId}/stats")
    Response getZoneStats(@PathParam("zoneId") Long zoneId);

}
