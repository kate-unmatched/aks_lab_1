package org.geodispatch.service;

import org.geodispatch.entity.TrackerPing;

import java.util.List;

public interface TrackerPingService extends CrudService<TrackerPing> {

    /**
     * Найти все GPS-пинги по ID машины.
     */
    List<TrackerPing> findByVehicle(Long vehicleId);

    /**
     * Найти последний (самый свежий) GPS-пинг по ID машины.
     */
    TrackerPing findLastPing(Long vehicleId);

    /**
     * Найти все пинги, которые попали в конкретную геозону.
     * (опционально, для геофенсинга)
     */
    List<TrackerPing> findInsideZone(Long zoneId);
}

