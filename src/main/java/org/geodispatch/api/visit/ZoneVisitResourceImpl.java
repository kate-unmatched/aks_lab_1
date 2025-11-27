package org.geodispatch.api.visit;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.AbstractCrudResource;

import org.geodispatch.entity.ZoneVisit;
import org.geodispatch.service.visit.ZoneVisitService;
import org.geodispatch.service.base.CrudService;

import java.time.Instant;
import java.util.List;

/**
 * Implementation of {@link ZoneVisitResource}.
 * <p>
 * Delegates CRUD operations to {@link ZoneVisitService}
 * and implements visit lifecycle operations.
 */
public class ZoneVisitResourceImpl extends AbstractCrudResource<ZoneVisit> implements ZoneVisitResource {

    @EJB
    private ZoneVisitService visitService;

    @Override
    protected CrudService<ZoneVisit> getService() {
        return visitService;
    }

    @Override
    protected void applyUpdates(ZoneVisit existing, ZoneVisit newData) {
        existing.setVehicle(newData.getVehicle());
        existing.setZone(newData.getZone());
        existing.setJobOrder(newData.getJobOrder());
        existing.setEnteredAt(newData.getEnteredAt());
        existing.setLeftAt(newData.getLeftAt());
        existing.setConfirmed(newData.isConfirmed());
    }

    @Override
    public List<ZoneVisit> getByVehicle(Long vehicleId) {
        return visitService.findByVehicle(vehicleId);
    }

    @Override
    public List<ZoneVisit> getByZone(Long zoneId) {
        return visitService.findByZone(zoneId);
    }

    @Override
    public List<ZoneVisit> getByOrder(Long orderId) {
        return visitService.findByOrder(orderId);
    }

    @Override
    public Response enter(Long id) {
        ZoneVisit v = visitService.enter(id, Instant.now());
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Visit not found")
                           .build();
        }
        return Response.ok(v).build();
    }

    @Override
    public Response leave(Long id) {
        ZoneVisit v = visitService.leave(id, Instant.now());
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Visit not found")
                           .build();
        }
        return Response.ok(v).build();
    }

    @Override
    public Response confirm(Long id) {
        ZoneVisit v = visitService.confirm(id);
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Visit not found")
                           .build();
        }
        return Response.ok(v).build();
    }
}
