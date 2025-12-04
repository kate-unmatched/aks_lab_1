package org.geodispatch.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.geodispatch.api.ZoneVisitApi;
import org.geodispatch.dtos.ZoneVisitUpdateRequestDTO;
import org.geodispatch.entity.ZoneVisit;
import org.geodispatch.mappers.ZoneVisitMapper;
import org.geodispatch.service.ZoneVisitService;

import java.util.List;

public class ZoneVisitApiImpl implements ZoneVisitApi {

    @EJB
    private ZoneVisitService service;

    @EJB
    private ZoneVisitMapper mapper;

    @Override
    public Response findAll() {
        List<ZoneVisit> visits = service.findAll();
        return Response.ok(visits).build();
    }

    @Override
    public Response findById(Long id) {
        ZoneVisit visit = service.findById(id);
        return visit != null
                ? Response.ok(visit).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response findByZone(Long zoneId) {
        List<ZoneVisit> visits = service.findByZone(zoneId);
        return Response.ok(visits).build();
    }

    @Override
    public Response findByVehicle(Long vehicleId) {
        List<ZoneVisit> visits = service.findByVehicle(vehicleId);
        return Response.ok(visits).build();
    }

    @Override
    public Response update(Long id, ZoneVisitUpdateRequestDTO dto) {
        ZoneVisit existing = service.findById(id);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ZoneVisit not found")
                    .build();
        }

        if (dto.getLeftAt() != null) {
            existing.setLeftAt(dto.getLeftAt());
        }

        if (dto.getConfirmed() != null) {
            existing.setConfirmed(dto.getConfirmed());
        }

        ZoneVisit updated = service.update(existing);
        return Response.ok(updated).build();
    }

    @Override
    public Response create(ZoneVisit visit) {
        ZoneVisit created = service.create(visit);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Override
    public Response delete(Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @Override
    public Response findLastByVehicle(Long vehicleId) {
        ZoneVisit visit = service.findLastByVehicle(vehicleId);

        return visit != null
                ? Response.ok(mapper.toResponseDto(visit)).build()
                : Response.status(Response.Status.NOT_FOUND)
                .entity("Last ZoneVisit not found for vehicle " + vehicleId)
                .build();
    }

    @Override
    public Response getZoneStats(Long zoneId) {
        var stats = service.getZoneStats(zoneId);

        if (stats == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Zone not found or no visits")
                    .build();
        }

        return Response.ok(stats).build();
    }

}
