package org.geodispatch.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.geodispatch.dtos.OrderDto;
import org.geodispatch.dtos.PlannedZoneBodyDto;
import org.geodispatch.mappers.DtoMapper;
import org.geodispatch.services.JobOrderService;
import static org.geodispatch.mappers.DtoMapper.*;

import java.net.URI;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobOrderResource {

    @Inject JobOrderService service;

    public record CreateOrderBody(Long vehicleId, Long plannedZoneId) { }

    @POST
    public Response create(CreateOrderBody body, @Context UriInfo info) {
        var o = service.create(body.vehicleId(), body.plannedZoneId());
        return Response.created(info.getAbsolutePathBuilder().path(String.valueOf(o.getId())).build())
                .entity(toDto(o)).build();
    }

    @GET @Path("{id}")
    public OrderDto get(@PathParam("id") long id) {
        return service.findById(id).map(DtoMapper::toDto).orElseThrow(NotFoundException::new);
    }

    @PUT @Path("{id}/start")
    public OrderDto start(@PathParam("id") long id) { return toDto(service.start(id)); }

    @PUT @Path("{id}/complete")
    public OrderDto complete(@PathParam("id") long id) { return toDto(service.complete(id)); }

    @PUT @Path("{id}/planned-zone")
    public OrderDto setPlanned(@PathParam("id") long id, PlannedZoneBodyDto body) {
        return toDto(service.setPlannedZone(id, body.zoneId()));
    }

    @GET
    @Path("active")
    public OrderDto active(@QueryParam("vehicleId") long vehicleId) {
        return service.findActiveForVehicle(vehicleId)
                .map(DtoMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }
}
