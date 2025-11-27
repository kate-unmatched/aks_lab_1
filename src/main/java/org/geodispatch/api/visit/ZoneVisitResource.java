package org.geodispatch.api.visit;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.CrudResource;
import org.geodispatch.entity.ZoneVisit;

import java.util.List;

/**
 * REST API for managing visits of vehicles inside geographic zones.
 * <p>
 * Provides CRUD operations (via {@link CrudResource}) and
 * business actions related to entering, leaving and confirming visits.
 */
@Path("/visits")
@Produces("application/json")
@Consumes("application/json")
public interface ZoneVisitResource extends CrudResource<ZoneVisit> {

    /**
     * Returns all visits for a specific vehicle.
     *
     * @param vehicleId vehicle identifier
     * @return list of visits
     */
    @GET
    @Path("/by-vehicle/{vehicleId}")
    List<ZoneVisit> getByVehicle(@PathParam("vehicleId") Long vehicleId);

    /**
     * Returns all visits that belong to the specified zone.
     *
     * @param zoneId zone identifier
     * @return list of visits
     */
    @GET
    @Path("/by-zone/{zoneId}")
    List<ZoneVisit> getByZone(@PathParam("zoneId") Long zoneId);

    /**
     * Returns all visits associated with a job order.
     *
     * @param orderId job order identifier
     * @return list of visits
     */
    @GET
    @Path("/by-order/{orderId}")
    List<ZoneVisit> getByOrder(@PathParam("orderId") Long orderId);

    /**
     * Marks the visit as started at the given time.
     *
     * @param id visit identifier
     * @return updated visit or 404 if not found
     */
    @POST
    @Path("/{id}/enter")
    Response enter(@PathParam("id") Long id);

    /**
     * Marks the visit as ended at the given time.
     *
     * @param id visit identifier
     * @return updated visit or 404 if not found
     */
    @POST
    @Path("/{id}/leave")
    Response leave(@PathParam("id") Long id);

    /**
     * Confirms a visit as valid.
     *
     * @param id visit identifier
     * @return updated visit or 404 if not found
     */
    @POST
    @Path("/{id}/confirm")
    Response confirm(@PathParam("id") Long id);
}
