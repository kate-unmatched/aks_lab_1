package org.geodispatch.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.entity.Vehicle;
import java.util.List;

/**
 * REST API for managing vehicles in the system.
 * <p>
 * Provides CRUD operations inherited from {@link CrudResource}
 * as well as additional vehicle-specific endpoints such as
 * retrieving active vehicles and obtaining the latest GPS ping.
 */
@Path("/vehicles")
@Produces("application/json")
@Consumes("application/json")
public interface VehicleResource extends CrudResource<Vehicle> {

    /**
     * Returns a list of all active vehicles.
     * <p>
     * A vehicle is considered active if it has at least one tracker ping
     * with speed greater than zero.
     *
     * @return list of active {@link Vehicle} objects
     *
     * @responseMessage 200 Successful operation
     */
    @GET
    @Path("/active")
    List<Vehicle> getActiveVehicles();

    /**
     * Returns the most recent GPS ping for a given vehicle.
     *
     * @param id identifier of the vehicle
     * @return {@link Response} containing the latest {@link TrackerPing}
     *         or HTTP 404 if the vehicle or ping does not exist
     *
     * @responseMessage 200 Last ping retrieved successfully
     * @responseMessage 404 Vehicle not found or no pings available
     */
    @GET
    @Path("/{id}/last-ping")
    Response getLastPing(@PathParam("id") Long id);
}

