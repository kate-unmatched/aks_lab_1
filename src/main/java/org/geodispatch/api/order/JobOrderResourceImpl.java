package org.geodispatch.api.order;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.AbstractCrudResource;

import org.geodispatch.entity.JobOrder;
import org.geodispatch.service.order.JobOrderService;
import org.geodispatch.service.base.CrudService;

import java.util.List;

/**
 * Implementation of {@link JobOrderResource}.
 * <p>
 * Extends {@link AbstractCrudResource} for generic CRUD
 * and provides business logic for job order lifecycle.
 */
public class JobOrderResourceImpl extends AbstractCrudResource<JobOrder> implements JobOrderResource {

    @EJB
    private JobOrderService orderService;

    @Override
    protected CrudService<JobOrder> getService() {
        return orderService;
    }

    @Override
    protected void applyUpdates(JobOrder existing, JobOrder newData) {
        existing.setVehicle(newData.getVehicle());
        existing.setStatus(newData.getStatus());
        existing.setPlannedZone(newData.getPlannedZone());
        existing.setStartedAt(newData.getStartedAt());
        existing.setCompletedAt(newData.getCompletedAt());
    }

    @Override
    public List<JobOrder> getByVehicle(Long vehicleId) {
        return orderService.findByVehicle(vehicleId);
    }

    @Override
    public List<JobOrder> getByStatus(JobOrder.Status status) {
        return orderService.findByStatus(status);
    }

    @Override
    public List<JobOrder> getActiveOrders() {
        return orderService.findActive();
    }

    @Override
    public Response startOrder(Long id) {
        JobOrder o = orderService.startOrder(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Order not found")
                           .build();
        }
        return Response.ok(o).build();
    }

    @Override
    public Response completeOrder(Long id) {
        JobOrder o = orderService.completeOrder(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Order not found")
                           .build();
        }
        return Response.ok(o).build();
    }
}
