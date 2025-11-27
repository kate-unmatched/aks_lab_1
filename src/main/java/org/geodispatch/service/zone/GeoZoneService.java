package org.geodispatch.service.zone;

import org.geodispatch.entity.GeoZone;
import org.geodispatch.service.base.CrudService;

import java.util.List;

/**
 * Business service for working with geographic zones.
 */
public interface GeoZoneService extends CrudService<GeoZone> {

    /**
     * Finds zones whose radius contains the specified coordinates.
     *
     * @param latitude  latitude in degrees
     * @param longitude longitude in degrees
     * @return list of matching geo zones
     */
    List<GeoZone> findContainingPoint(double latitude, double longitude);

    /**
     * Finds zone by unique name.
     *
     * @param name zone name
     * @return GeoZone or null if not found
     */
    GeoZone findByName(String name);
}
