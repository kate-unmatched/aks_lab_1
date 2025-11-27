package org.geodispatch.api.ping;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.geodispatch.api.base.CrudResource;
import org.geodispatch.entity.TrackerPing;

import java.util.List;

/**
 * REST API for working with GPS tracker pings.
 * <p>
 * Provides CRUD operations inherited from {@link CrudResource}
 * as well as additional endpoints for retrieving pings
 * associated with a specific vehicle.
 */
@Path("/pings")
@Produces("application/json")
@Consumes("application/json")
public interface TrackerPingResource extends CrudResource<TrackerPing> {

    /**
     * Returns all tracker pings associated with the given vehicle.
     *
     * @param vehicleId identifier of the vehicle
     * @return list of {@link TrackerPing} objects sorted by timestamp in descending order
     *
     * @responseMessage 200 Successful operation
     */
    @GET
    @Path("/by-vehicle/{vehicleId}")
    List<TrackerPing> getByVehicle(@PathParam("vehicleId") Long vehicleId);

    /**
     * Returns the most recent tracker ping for the specified vehicle.
     *
     * @param vehicleId identifier of the vehicle
     * @return {@link Response} containing the latest {@link TrackerPing}
     *         or HTTP 404 if no pings exist for the given vehicle
     *
     * @responseMessage 200 Last ping retrieved successfully
     * @responseMessage 404 No pings found for the vehicle
     */
    @GET
    @Path("/by-vehicle/{vehicleId}/last")
    Response getLast(@PathParam("vehicleId") Long vehicleId);
}
