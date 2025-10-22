package org.geodispatch.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.JobOrder;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.entity.ZoneVisit;

import java.time.Instant;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VisitService {

    @PersistenceContext(unitName = "geoPU")
    private EntityManager em;

    /** Создаёт подтверждённый визит, если такого ещё не было (idempotent по vehicle+zone+order в заданном интервале). */
    public ZoneVisit confirmVisit(long vehicleId, long zoneId, Long orderId, Instant enteredAt, Instant leftAt) {
        Vehicle v = em.getReference(Vehicle.class, vehicleId);
        GeoZone z = em.getReference(GeoZone.class, zoneId);
        JobOrder o = (orderId != null) ? em.getReference(JobOrder.class, orderId) : null;

        Long already = em.createQuery("""
                select count(vs) from ZoneVisit vs
                where vs.vehicle = :v and vs.zone = :z and vs.jobOrder = :o and vs.confirmed = true
                  and vs.enteredAt <= :left and coalesce(vs.leftAt, vs.enteredAt) >= :entered
                """, Long.class)
            .setParameter("v", v)
            .setParameter("z", z)
            .setParameter("o", o)
            .setParameter("entered", enteredAt)
            .setParameter("left", leftAt != null ? leftAt : enteredAt)
            .getSingleResult();

        if (already > 0) {
            // возвращаем любой имеющийся визит
            return em.createQuery("""
                    select vs from ZoneVisit vs
                    where vs.vehicle = :v and vs.zone = :z and vs.jobOrder = :o and vs.confirmed = true
                    order by vs.enteredAt desc
                    """, ZoneVisit.class)
                .setParameter("v", v).setParameter("z", z).setParameter("o", o)
                .setMaxResults(1).getSingleResult();
        }

        ZoneVisit visit = new ZoneVisit();
        visit.setVehicle(v);
        visit.setZone(z);
        visit.setJobOrder(o);
        visit.setEnteredAt(enteredAt);
        visit.setLeftAt(leftAt);
        visit.setConfirmed(true);
        em.persist(visit);
        return visit;
    }

    public List<ZoneVisit> listForVehicle(long vehicleId, int offset, int limit) {
        var q = em.createQuery("""
                select vs from ZoneVisit vs
                where vs.vehicle.id = :vid
                order by vs.enteredAt desc
                """, ZoneVisit.class)
            .setParameter("vid", vehicleId);
        if (offset > 0) q.setFirstResult(offset);
        if (limit > 0) q.setMaxResults(limit);
        return q.getResultList();
    }

    public List<ZoneVisit> listForZone(long zoneId, int offset, int limit) {
        var q = em.createQuery("""
                select vs from ZoneVisit vs
                where vs.zone.id = :zid
                order by vs.enteredAt desc
                """, ZoneVisit.class)
            .setParameter("zid", zoneId);
        if (offset > 0) q.setFirstResult(offset);
        if (limit > 0) q.setMaxResults(limit);
        return q.getResultList();
    }
}
