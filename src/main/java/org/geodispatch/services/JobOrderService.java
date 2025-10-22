package org.geodispatch.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.JobOrder;
import org.geodispatch.entity.Vehicle;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JobOrderService {

    @PersistenceContext(unitName = "geoPU")
    private EntityManager em;

    public JobOrder create(Long vehicleId, Long plannedZoneId) {
        Vehicle v = (vehicleId != null) ? em.getReference(Vehicle.class, vehicleId) : null;
        GeoZone z = (plannedZoneId != null) ? em.getReference(GeoZone.class, plannedZoneId) : null;

        JobOrder o = new JobOrder();
        o.setVehicle(v);
        o.setPlannedZone(z);
        o.setStatus(JobOrder.Status.PLANNED);
        em.persist(o);
        return o;
    }

    public JobOrder start(long orderId) {
        JobOrder o = getOrThrow(orderId);
        if (o.getStatus() == JobOrder.Status.IN_PROGRESS) return o;
        if (o.getStatus() == JobOrder.Status.COMPLETED)
            throw new IllegalStateException("Order already completed");
        o.setStatus(JobOrder.Status.IN_PROGRESS);
        o.setStartedAt(Instant.now());
        return o;
    }

    public JobOrder complete(long orderId) {
        JobOrder o = getOrThrow(orderId);
        if (o.getStatus() == JobOrder.Status.COMPLETED) return o;
        o.setStatus(JobOrder.Status.COMPLETED);
        if (o.getStartedAt() == null) o.setStartedAt(Instant.now());
        o.setCompletedAt(Instant.now());
        return o;
    }

    public Optional<JobOrder> findById(long id) {
        return Optional.ofNullable(em.find(JobOrder.class, id));
    }

    public Optional<JobOrder> findActiveForVehicle(long vehicleId) {
        List<JobOrder> list = em.createQuery("""
                select o from JobOrder o
                where o.vehicle.id = :vid and o.status in (:s1, :s2)
                order by o.id desc
                """, JobOrder.class)
            .setParameter("vid", vehicleId)
            .setParameter("s1", JobOrder.Status.IN_PROGRESS)
            .setParameter("s2", JobOrder.Status.PLANNED)
            .setMaxResults(1)
            .getResultList();
        return list.stream().findFirst();
    }

    public JobOrder setPlannedZone(long orderId, Long zoneId) {
        JobOrder o = getOrThrow(orderId);
        GeoZone z = (zoneId != null) ? em.getReference(GeoZone.class, zoneId) : null;
        o.setPlannedZone(z);
        return o;
    }

    private JobOrder getOrThrow(long id) {
        JobOrder o = em.find(JobOrder.class, id);
        if (o == null) throw new NoSuchElementException("JobOrder not found: id=" + id);
        return o;
    }
}
