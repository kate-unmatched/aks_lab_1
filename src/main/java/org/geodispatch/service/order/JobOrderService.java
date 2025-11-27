package org.geodispatch.service.order;

import org.geodispatch.entity.JobOrder;
import org.geodispatch.service.base.CrudService;

import java.util.List;

/**
 * Business service for working with job orders.
 */
public interface JobOrderService extends CrudService<JobOrder> {

    /**
     * Finds all job orders assigned to the given vehicle.
     *
     * @param vehicleId ID of the vehicle
     * @return list of job orders
     */
    List<JobOrder> findByVehicle(Long vehicleId);

    /**
     * Finds all job orders with the specified status.
     *
     * @param status job order status
     * @return list of matching orders
     */
    List<JobOrder> findByStatus(JobOrder.Status status);

    /**
     * Returns all not completed job orders.
     *
     * @return list of active (non-finished) job orders
     */
    List<JobOrder> findActive();

    /**
     * Marks an order as started (sets status, timestamp).
     *
     * @param orderId order ID
     * @return updated order or null if not found
     */
    JobOrder startOrder(Long orderId);

    /**
     * Marks an order as completed (sets status, timestamp).
     *
     * @param orderId order ID
     * @return updated order or null if not found
     */
    JobOrder completeOrder(Long orderId);
}
