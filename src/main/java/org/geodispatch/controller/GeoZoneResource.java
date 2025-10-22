package org.geodispatch.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.services.GeoZoneService;

import java.util.List;

@Path("/zones")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GeoZoneResource {
    @Inject
    GeoZoneService service;

    @POST
    public GeoZone create(GeoZone dto) {
        return service.create(dto.getName(), dto.getLatitude(), dto.getLongitude(), dto.getRadiusMeters());
    }

    @GET
    public List<GeoZone> list(@QueryParam("offset") @DefaultValue("0") int offset,
                              @QueryParam("limit")  @DefaultValue("50") int limit) {
        return service.list(offset, limit);
    }

    @GET
    @Path("{id}")
    public GeoZone get(@PathParam("id") long id) { return service.findById(id).orElseThrow(NotFoundException::new); }

    @PUT
    @Path("{id}")
    public GeoZone update(@PathParam("id") long id, GeoZone dto) {
        return service.update(id, dto.getName(), dto.getLatitude(), dto.getLongitude(), dto.getRadiusMeters());
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) { service.delete(id); }

    @GET
    @Path("contains")
    public List<GeoZone> contains(@QueryParam("lat") double lat, @QueryParam("lon") double lon) {
        return service.findZonesContaining(lat, lon);
    }
}
