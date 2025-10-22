package org.geodispatch.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.dtos.PingDto;
import org.geodispatch.dtos.PingResultDto;
import org.geodispatch.services.GeoZoneService;
import org.geodispatch.services.JobOrderService;
import org.geodispatch.services.TrackerService;
import static org.geodispatch.mappers.DtoMapper.*;
import java.time.Instant;
import java.util.List;

@Path("/tracker")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrackerResource {

    @Inject
    TrackerService trackerService;
    @Inject
    GeoZoneService zoneService;
    @Inject
    JobOrderService orderService;

    @POST @Path("/pings")
    public Response ingest(@Valid PingDto dto) {
        var ts = java.time.Instant.parse(dto.timestamp());
        var ping = trackerService.ingestPing(
                dto.vehicleId(),
                ts,
                dto.latitude(),
                dto.longitude(),
                dto.speedKmh(),
                dto.headingDegrees()
        );

        var matched = zoneService.findZonesContaining(dto.latitude(), dto.longitude());
        var zones = matched.stream()
                .map(z -> toMatched(z, GeoZoneService.distanceMeters(z.getLatitude(), z.getLongitude(), dto.latitude(), dto.longitude())))
                .toList();

        Boolean completed = orderService.findActiveForVehicle(dto.vehicleId())
                .map(o -> o.getStatus() == org.geodispatch.entity.JobOrder.Status.COMPLETED)
                .orElse(null);

        return Response.status(Response.Status.CREATED)
                .entity(new PingResultDto(ping.getId(), zones, completed))
                .build();
    }
}
