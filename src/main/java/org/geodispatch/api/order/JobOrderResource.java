package org.geodispatch.api.order;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import org.geodispatch.api.base.CrudResource;
import org.geodispatch.entity.JobOrder;

import java.util.List;

/**
 * REST API for managing job orders.
 * <p>
 * Provides standard CRUD operations inherited from {@link CrudResource}
 * and additional endpoints for business actions: start, complete,
 * and filter orders by vehicle or status.
 */
@Path("/orders")
@Produces("application/json")
@Consumes("application/json")
public interface JobOrderResource extends CrudResource<JobOrder> {

    /**
     * Returns all job orders assigned to the given vehicle.
     *
     * @param vehicleId vehicle identifier
     * @return list of job orders for the vehicle
     *
     * @responseMessage 200 Successful operation
     */
    @GET
    @Path("/by-vehicle/{vehicleId}")
    List<JobOrder> getByVehicle(@PathParam("vehicleId") Long vehicleId);

    /**
     * Returns all job orders with a specified status.
     *
     * @param status job order status
     * @return list of matching job orders
     *
     * @responseMessage 200 Successful operation
     */
    @GET
    @Path("/status/{status}")
    List<JobOrder> getByStatus(@PathParam("status") JobOrder.Status status);

    /**
     * Returns all non-completed (active) job orders.
     *
     * @return list of active orders
     *
     * @responseMessage 200 Successful operation
     */
    @GET
    @Path("/active")
    List<JobOrder> getActiveOrders();

    /**
     * Marks an order as started.
     *
     * @param id order ID
     * @return updated order or HTTP 404 if not found
     *
     * @responseMessage 200 Order successfully started
     * @responseMessage 404 Order not found
     */
    @POST
    @Path("/{id}/start")
    Response startOrder(@PathParam("id") Long id);

    /**
     * Marks an order as completed.
     *
     * @param id order ID
     * @return updated order or HTTP 404 if not found
     *
     * @responseMessage 200 Order successfully completed
     * @responseMessage 404 Order not found
     */
    @POST
    @Path("/{id}/complete")
    Response completeOrder(@PathParam("id") Long id);
}
