package org.geodispatch.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import org.geodispatch.dtos.ZoneStatsDTO;
import org.geodispatch.dtos.ZoneVisitZoneStatsDTO;
import org.geodispatch.entity.ZoneVisit;

import java.time.Instant;
import java.time.LocalDateTime;
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
    public ZoneVisit enter(Long visitId, LocalDateTime timestamp) {
        ZoneVisit v = findById(visitId);
        if (v == null) return null;

        v.setEnteredAt(timestamp);
        return update(v);
    }

    @Override
    public ZoneVisit leave(Long visitId, LocalDateTime timestamp) {
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

    @Override
    public ZoneVisit findLastByVehicle(Long vehicleId) {
        TypedQuery<ZoneVisit> q = em.createQuery(
                "select v from ZoneVisit v " +
                        "where v.vehicle.id = :vehicleId " +
                        "order by v.enteredAt desc", ZoneVisit.class
        );
        q.setParameter("vehicleId", vehicleId);
        q.setMaxResults(1);

        List<ZoneVisit> result = q.getResultList();
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public ZoneVisitZoneStatsDTO getZoneStats(Long zoneId) {

        List<ZoneVisit> visits = findByZone(zoneId);

        if (visits.isEmpty())
            return null;

        ZoneVisitZoneStatsDTO dto = new ZoneVisitZoneStatsDTO();
        dto.setZoneId(zoneId);

        dto.setTotalVisits(visits.size());

        dto.setUniqueVehicles(
                visits.stream()
                        .map(v -> v.getVehicle().getId())
                        .distinct()
                        .count()
        );

        dto.setIncompleteVisits(
                visits.stream()
                        .filter(v -> v.getLeftAt() == null)
                        .count()
        );

        dto.setAvgDurationMinutes(
                visits.stream()
                        .filter(v -> v.getLeftAt() != null)
                        .mapToLong(v -> java.time.Duration.between(
                                v.getEnteredAt(),
                                v.getLeftAt()
                        ).toMinutes())
                        .average()
                        .orElse(0)
        );

        return dto;
    }


    @Override
    public List<ZoneStatsDTO> getAllZonesStats() {
        List<ZoneVisit> visits = findAll();

        return visits.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        v -> v.getZone().getId()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    Long zoneId = entry.getKey();
                    List<ZoneVisit> zoneVisits = entry.getValue();

                    ZoneStatsDTO dto = new ZoneStatsDTO();
                    dto.setZoneId(zoneId);
                    dto.setTotalVisits(zoneVisits.size());

                    dto.setUniqueVehicles(
                            zoneVisits.stream()
                                    .map(v -> v.getVehicle().getId())
                                    .distinct()
                                    .count()
                    );

                    return dto;
                })
                .toList();
    }


}
