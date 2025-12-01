package org.geodispatch.service;

import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.Vehicle;

import java.util.List;

/**
 * Business service for working with geographic zones.
 */
public interface GeoZoneService extends CrudService<GeoZone> {

    boolean contains(Long zoneId, double lat, double lon);

    List<Vehicle> findVehiclesInsideZone(Long zoneId);
}
