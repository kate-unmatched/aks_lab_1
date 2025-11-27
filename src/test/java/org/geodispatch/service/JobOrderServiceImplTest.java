package org.geodispatch.service;

import org.geodispatch.entity.JobOrder;
import org.geodispatch.service.order.JobOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobOrderServiceImplTest {

    private JobOrderServiceImpl service;
    private EntityManager em;

    @BeforeEach
    void setup() {
        service = new JobOrderServiceImpl();
        em = mock(EntityManager.class);
        service.em = em;
    }

    /**
     * Ensures that when an order is started:
     * - its status changes to IN_PROGRESS
     * - the start timestamp is set
     * - the updated entity is returned
     */
    @Test
    void testStartOrder() {
        JobOrder order = new JobOrder();
        order.setId(1L);
        order.setStatus(JobOrder.Status.PLANNED);

        when(em.find(JobOrder.class, 1L)).thenReturn(order);
        when(em.merge(order)).thenReturn(order); // ВАЖНО!

        JobOrder updated = service.startOrder(1L);

        assertNotNull(updated);
        assertEquals(JobOrder.Status.IN_PROGRESS, updated.getStatus());
        assertNotNull(updated.getStartedAt());
    }

    /**
     * Ensures that completing an order:
     * - sets status to COMPLETED
     * - fills the completion timestamp
     * - returns the modified order
     */
    @Test
    void testCompleteOrder() {
        JobOrder order = new JobOrder();
        order.setId(1L);
        order.setStatus(JobOrder.Status.IN_PROGRESS);
        order.setStartedAt(Instant.now());

        when(em.find(JobOrder.class, 1L)).thenReturn(order);
        when(em.merge(order)).thenReturn(order);

        JobOrder updated = service.completeOrder(1L);

        assertNotNull(updated);
        assertEquals(JobOrder.Status.COMPLETED, updated.getStatus());
        assertNotNull(updated.getCompletedAt());
    }
}
