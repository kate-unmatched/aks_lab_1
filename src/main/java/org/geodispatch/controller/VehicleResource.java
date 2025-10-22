package org.geodispatch.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.geodispatch.dtos.VehicleDto;
import static org.geodispatch.mappers.DtoMapper.*;

import org.geodispatch.mappers.DtoMapper;
import org.geodispatch.services.VehicleService;
import org.geodispatch.utils.PageDto;

import java.net.URI;
import java.util.List;

@Path("/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleResource {

    @Inject
    VehicleService service;

    @GET
    public PageDto<VehicleDto> list(@QueryParam("offset") @DefaultValue("0") int offset,
                                    @QueryParam("limit")  @DefaultValue("50") int limit) {
        var items = service.list(offset, limit);
        var total = service.countAll();
        return toPage(items, total, offset, limit, DtoMapper::toDto);
    }

    @POST
    public Response create(@Valid VehicleDto body, @Context UriInfo info) {
        var v = service.create(body.registrationPlate(), body.model(), body.manufacturer());
        var loc = info.getAbsolutePathBuilder().path(String.valueOf(v.getId())).build();
        return Response.created(loc).entity(toDto(v)).build();
    }

    @GET @Path("{id}")
    public VehicleDto get(@PathParam("id") long id) {
        return service.findById(id).map(DtoMapper::toDto).orElseThrow(NotFoundException::new);
    }

    @PUT @Path("{id}")
    public VehicleDto update(@PathParam("id") long id, VehicleDto body) {
        return toDto(service.update(id, body.registrationPlate(), body.model(), body.manufacturer()));
    }

    @DELETE @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("lookup")
    public VehicleDto byPlate(@QueryParam("plate") String plate) {
        return service.findByPlate(plate)
                .map(DtoMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }
}
