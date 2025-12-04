package org.geodispatch.service;

import org.geodispatch.dtos.ZoneStatsDTO;
import org.geodispatch.dtos.ZoneVisitZoneStatsDTO;
import org.geodispatch.entity.ZoneVisit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Business service for managing vehicle visits to geographic zones.
 */
public interface ZoneVisitService extends CrudService<ZoneVisit> {

    /**
     * Finds all visits for the given vehicle.
     *
     * @param vehicleId vehicle identifier
     * @return list of visits
     */
    List<ZoneVisit> findByVehicle(Long vehicleId);

    /**
     * Finds all visits for the given zone.
     *
     * @param zoneId zone identifier
     * @return list of visits
     */
    List<ZoneVisit> findByZone(Long zoneId);

    /**
     * Finds all visits associated with a specific job order.
     *
     * @param orderId job order identifier
     * @return list of visits
     */
    List<ZoneVisit> findByOrder(Long orderId);

    /**
     * Marks the visit as started (vehicle entered the zone).
     *
     * @param visitId visit identifier
     * @param timestamp time of zone entry
     * @return updated visit or null if not found
     */
    ZoneVisit enter(Long visitId, LocalDateTime timestamp);

    /**
     * Marks the visit as finished (vehicle left the zone).
     *
     * @param visitId visit identifier
     * @param timestamp time of leaving
     * @return updated visit or null if not found
     */
    ZoneVisit leave(Long visitId, LocalDateTime timestamp);

    /**
     * Confirms this visit as valid.
     *
     * @param visitId visit identifier
     * @return updated visit or null if not found
     */
    ZoneVisit confirm(Long visitId);

    ZoneVisit findLastByVehicle(Long vehicleId);

    ZoneVisitZoneStatsDTO getZoneStats(Long zoneId);

    List<ZoneStatsDTO> getAllZonesStats();
}
