package org.geodispatch.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.geodispatch.dtos.VisitDto;
import org.geodispatch.entity.ZoneVisit;
import org.geodispatch.mappers.DtoMapper;
import org.geodispatch.services.VisitService;
import org.geodispatch.utils.ErrorDto;
import org.geodispatch.utils.PageDto;
import static org.geodispatch.mappers.DtoMapper.*;

import java.time.Instant;
import java.util.List;

@Path("/visits")
@Produces(MediaType.APPLICATION_JSON)
public class VisitResource {

    @Inject
    VisitService service;

    @GET
    public PageDto<VisitDto> list(@QueryParam("vehicleId") Long vehicleId,
                                  @QueryParam("zoneId") Long zoneId,
                                  @QueryParam("offset") @DefaultValue("0") int offset,
                                  @QueryParam("limit")  @DefaultValue("50") int limit) {
        if (vehicleId != null) {
            var items = service.listForVehicle(vehicleId, offset, limit);
            return toPage(items, items.size(), offset, limit, DtoMapper::toDto);
        } else if (zoneId != null) {
            var items = service.listForZone(zoneId, offset, limit);
            return toPage(items, items.size(), offset, limit, DtoMapper::toDto);
        }
        throw new BadRequestException("Specify vehicleId or zoneId");
    }

    public record ConfirmBody(long zoneId, long vehicleId, Long orderId, String from, String to,
                              Integer minDwellSec, Integer gapSec, Double maxSpeedKmh) {}

    @POST @Path("/confirm")
    @Consumes(MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response confirm(ConfirmBody body) {
        boolean ok = service.confirmVisitByDwell( // если confirm переносила в VisitService — дерни оттуда
                body.zoneId(), body.vehicleId(),
                Instant.parse(body.from()), Instant.parse(body.to()),
                body.minDwellSec() != null ? body.minDwellSec() : 30,
                body.gapSec() != null ? body.gapSec() : 15,
                body.maxSpeedKmh());
        if (ok) return jakarta.ws.rs.core.Response.ok().build();
        return jakarta.ws.rs.core.Response.status(409).entity(
                ErrorDto.of("NOT_CONFIRMED", "Not enough dwell inside zone")).build();
    }

    private VisitDto toDto(ZoneVisit v) {
        return new VisitDto(
                v.getId(),
                v.getVehicle().getId(),
                v.getZone().getId(),
                v.getJobOrder() != null ? v.getJobOrder().getId() : null,
                v.getEnteredAt(), v.getLeftAt(), v.isConfirmed()
        );
    }
}
