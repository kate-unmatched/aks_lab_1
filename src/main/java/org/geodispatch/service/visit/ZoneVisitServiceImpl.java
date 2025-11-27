package org.geodispatch.service.visit;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import org.geodispatch.entity.ZoneVisit;
import org.geodispatch.service.base.CrudServiceImpl;

import java.time.Instant;
import java.util.List;

@Stateless
public class ZoneVisitServiceImpl extends CrudServiceImpl<ZoneVisit> implements ZoneVisitService {

    public ZoneVisitServiceImpl() {
        super(ZoneVisit.class);
    }

    @Override
    public List<ZoneVisit> findByVehicle(Long vehicleId) {
        TypedQuery<ZoneVisit> q = em.createQuery(
                "SELECT v FROM ZoneVisit v WHERE v.vehicle.id = :id",
                ZoneVisit.class
        );
        q.setParameter("id", vehicleId);
        return q.getResultList();
    }

    @Override
    public List<ZoneVisit> findByZone(Long zoneId) {
        TypedQuery<ZoneVisit> q = em.createQuery(
                "SELECT v FROM ZoneVisit v WHERE v.zone.id = :id",
                ZoneVisit.class
        );
        q.setParameter("id", zoneId);
        return q.getResultList();
    }

    @Override
    public List<ZoneVisit> findByOrder(Long orderId) {
        TypedQuery<ZoneVisit> q = em.createQuery(
                "SELECT v FROM ZoneVisit v WHERE v.jobOrder.id = :id",
                ZoneVisit.class
        );
        q.setParameter("id", orderId);
        return q.getResultList();
    }

    @Override
    public ZoneVisit enter(Long visitId, Instant timestamp) {
        ZoneVisit v = findById(visitId);
        if (v == null) return null;

        v.setEnteredAt(timestamp);
        return update(v);
    }

    @Override
    public ZoneVisit leave(Long visitId, Instant timestamp) {
        ZoneVisit v = findById(visitId);
        if (v == null) return null;

        v.setLeftAt(timestamp);
        return update(v);
    }

    @Override
    public ZoneVisit confirm(Long visitId) {
        ZoneVisit v = findById(visitId);
        if (v == null) return null;

        v.setConfirmed(true);
        return update(v);
    }
}
