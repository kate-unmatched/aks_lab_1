package org.geodispatch.api.zone;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.AbstractCrudResource;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.service.zone.GeoZoneService;
import org.geodispatch.service.base.CrudService;

import java.util.List;

/**
 * Implementation of {@link GeoZoneResource}.
 * <p>
 * Delegates CRUD operations to {@link GeoZoneService}
 * and provides geographic lookup functionality.
 */
public class GeoZoneResourceImpl extends AbstractCrudResource<GeoZone> implements GeoZoneResource {

    @EJB
    private GeoZoneService zoneService;

    @Override
    protected CrudService<GeoZone> getService() {
        return zoneService;
    }

    @Override
    protected void applyUpdates(GeoZone existing, GeoZone newData) {
        existing.setName(newData.getName());
        existing.setLatitude(newData.getLatitude());
        existing.setLongitude(newData.getLongitude());
        existing.setRadiusMeters(newData.getRadiusMeters());
    }

    @Override
    public List<GeoZone> findContainingPoint(double lat, double lon) {
        return zoneService.findContainingPoint(lat, lon);
    }

    @Override
    public Response getByName(String name) {
        GeoZone zone = zoneService.findByName(name);

        if (zone == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Zone not found: " + name)
                           .build();
        }

        return Response.ok(zone).build();
    }
}
