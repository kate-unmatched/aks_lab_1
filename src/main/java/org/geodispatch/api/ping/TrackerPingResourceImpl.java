package org.geodispatch.api.ping;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.AbstractCrudResource;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.service.ping.TrackerPingService;
import org.geodispatch.service.base.CrudService;

import java.util.List;

/**
 * Implementation of {@link TrackerPingResource}.
 * <p>
 * Extends {@link AbstractCrudResource} to reuse generic CRUD logic
 * and adds TrackerPing-specific operations.
 */
public class TrackerPingResourceImpl extends AbstractCrudResource<TrackerPing> implements TrackerPingResource {

    @EJB
    private TrackerPingService pingService;

    @Override
    protected CrudService<TrackerPing> getService() {
        return pingService;
    }

    @Override
    protected void applyUpdates(TrackerPing existing, TrackerPing newData) {
        existing.setTimestamp(newData.getTimestamp());
        existing.setLatitude(newData.getLatitude());
        existing.setLongitude(newData.getLongitude());
        existing.setSpeedKmh(newData.getSpeedKmh());
        existing.setHeadingDegrees(newData.getHeadingDegrees());
        existing.setVehicle(newData.getVehicle());
    }

    @Override
    public List<TrackerPing> getByVehicle(Long vehicleId) {
        return pingService.findByVehicle(vehicleId);
    }

    @Override
    public Response getLast(Long vehicleId) {
        List<TrackerPing> pings = pingService.findByVehicle(vehicleId);

        if (pings == null || pings.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No pings found for this vehicle")
                    .build();
        }

        // Первый элемент — самый свежий (ORDER BY timestamp DESC)
        return Response.ok(pings.get(0)).build();
    }
}
