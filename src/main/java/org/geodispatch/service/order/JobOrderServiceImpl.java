package org.geodispatch.service.order;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import org.geodispatch.entity.JobOrder;
import org.geodispatch.service.base.CrudServiceImpl;

import java.time.Instant;
import java.util.List;

@Stateless
public class JobOrderServiceImpl extends CrudServiceImpl<JobOrder> implements JobOrderService {

    public JobOrderServiceImpl() {
        super(JobOrder.class);
    }

    @Override
    public List<JobOrder> findByVehicle(Long vehicleId) {
        TypedQuery<JobOrder> q = em.createQuery(
                "SELECT o FROM JobOrder o WHERE o.vehicle.id = :id",
                JobOrder.class
        );
        q.setParameter("id", vehicleId);
        return q.getResultList();
    }

    @Override
    public List<JobOrder> findByStatus(JobOrder.Status status) {
        TypedQuery<JobOrder> q = em.createQuery(
                "SELECT o FROM JobOrder o WHERE o.status = :st",
                JobOrder.class
        );
        q.setParameter("st", status);
        return q.getResultList();
    }

    @Override
    public List<JobOrder> findActive() {
        TypedQuery<JobOrder> q = em.createQuery(
                "SELECT o FROM JobOrder o WHERE o.status <> :completed",
                JobOrder.class
        );
        q.setParameter("completed", JobOrder.Status.COMPLETED);
        return q.getResultList();
    }

    @Override
    public JobOrder startOrder(Long orderId) {
        JobOrder o = findById(orderId);
        if (o == null) return null;

        o.setStatus(JobOrder.Status.IN_PROGRESS);
        o.setStartedAt(Instant.now());

        return update(o);
    }

    @Override
    public JobOrder completeOrder(Long orderId) {
        JobOrder o = findById(orderId);
        if (o == null) return null;

        o.setStatus(JobOrder.Status.COMPLETED);
        o.setCompletedAt(Instant.now());

        return update(o);
    }
}
