package org.geodispatch.api.zone;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.CrudResource;
import org.geodispatch.entity.GeoZone;

import java.util.List;

/**
 * REST API for managing geographic zones.
 * <p>
 * Provides basic CRUD operations inherited from {@link CrudResource}
 * and additional operations for searching zones by coordinates.
 */
@Path("/zones")
@Produces("application/json")
@Consumes("application/json")
public interface GeoZoneResource extends CrudResource<GeoZone> {

    /**
     * Returns all zones that contain the given coordinates.
     * <p>
     * Checks whether the point (latitude, longitude) lies within
     * each zone’s radius.
     *
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     * @return list of zones containing the point
     *
     * @responseMessage 200 Successful operation
     */
    @GET
    @Path("/search")
    List<GeoZone> findContainingPoint(
            @QueryParam("lat") double lat,
            @QueryParam("lon") double lon
    );

    /**
     * Returns the zone by its unique name.
     *
     * @param name zone name
     * @return {@link Response} with the zone or HTTP 404 if not found
     *
     * @responseMessage 200 Zone found
     * @responseMessage 404 Zone not found
     */
    @GET
    @Path("/by-name/{name}")
    Response getByName(@PathParam("name") String name);
}
