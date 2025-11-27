package org.geodispatch.service.ping;

import org.geodispatch.entity.TrackerPing;
import org.geodispatch.service.base.CrudService;

import java.util.List;

public interface TrackerPingService extends CrudService<TrackerPing> {

    /**
     * Найти все GPS-пинги по ID машины.
     */
    List<TrackerPing> findByVehicle(Long vehicleId);
}
