package org.geodispatch.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.geodispatch.dtos.ZoneDto;
import org.geodispatch.mappers.DtoMapper;
import org.geodispatch.services.GeoZoneService;
import org.geodispatch.utils.PageDto;
import static org.geodispatch.mappers.DtoMapper.*;

import java.util.List;

@Path("/zones")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GeoZoneResource {

    @Inject
    GeoZoneService service;

    @GET
    public PageDto<ZoneDto> list(@QueryParam("offset") @DefaultValue("0") int offset,
                                 @QueryParam("limit")  @DefaultValue("50") int limit) {
        var items = service.list(offset, limit);
        var total = service.countAll();
        return toPage(items, total, offset, limit, DtoMapper::toDto);
    }

    @POST
    public Response create(@Valid ZoneDto body, @Context UriInfo info) {
        var z = service.create(body.name(), body.latitude(), body.longitude(), body.radiusMeters());
        var loc = info.getAbsolutePathBuilder().path(String.valueOf(z.getId())).build();
        return Response.created(loc).entity(toDto(z)).build();
    }

    @GET @Path("{id}")
    public ZoneDto get(@PathParam("id") long id) {
        return service.findById(id).map(DtoMapper::toDto).orElseThrow(NotFoundException::new);
    }

    @PUT @Path("{id}")
    public ZoneDto update(@PathParam("id") long id, ZoneDto body) {
        return toDto(service.update(id, body.name(), body.latitude(), body.longitude(), body.radiusMeters()));
    }

    @GET @Path("contains")
    public List<ZoneDto> contains(@QueryParam("lat") double lat, @QueryParam("lon") double lon) {
        return mapList(service.findZonesContaining(lat, lon), DtoMapper::toDto);
    }

    @PUT @Path("{id}/retune")
    public ZoneDto retune(@PathParam("id") long id,
                          @QueryParam("days")   @DefaultValue("14") int lookbackDays,
                          @QueryParam("q")      @DefaultValue("0.9") double quantile,
                          @QueryParam("margin") @DefaultValue("7")   double safetyMarginMeters,
                          @QueryParam("min")    @DefaultValue("8")   double minRadius,
                          @QueryParam("max")    @DefaultValue("120") double maxRadius,
                          @QueryParam("alpha")  @DefaultValue("0.35") double emaAlpha) {
        return toDto(service.retuneRadius(id, lookbackDays, quantile, safetyMarginMeters, minRadius, maxRadius, emaAlpha));
    }
}
