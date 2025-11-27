package org.geodispatch.service;

import jakarta.persistence.EntityManager;
import org.geodispatch.entity.ZoneVisit;
import org.geodispatch.service.visit.ZoneVisitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ZoneVisitServiceImplTest {

    private ZoneVisitServiceImpl service;

    @BeforeEach
    void setup() {
        service = new ZoneVisitServiceImpl();
        service.em = mock(EntityManager.class);
    }

    /**
     * Ensures that calling enter() sets the enteredAt timestamp.
     */
    @Test
    void testEnter() {
        ZoneVisit v = new ZoneVisit();
        v.setId(1L);

        when(service.findById(1L)).thenReturn(v);
        when(service.em.merge(v)).thenReturn(v);

        ZoneVisit updated = service.enter(1L, Instant.now());

        assertNotNull(updated.getEnteredAt());
    }

    /**
     * Ensures that leave() sets the leftAt timestamp properly.
     */
    @Test
    void testLeave() {
        ZoneVisit v = new ZoneVisit();
        v.setId(1L);

        when(service.findById(1L)).thenReturn(v);
        when(service.em.merge(v)).thenReturn(v);

        ZoneVisit updated = service.leave(1L, Instant.now());

        assertNotNull(updated.getLeftAt());
    }

    /**
     * Verifies that calling confirm() marks visit as confirmed.
     */
    @Test
    void testConfirm() {
        ZoneVisit v = new ZoneVisit();
        v.setId(1L);

        when(service.findById(1L)).thenReturn(v);
        when(service.em.merge(v)).thenReturn(v);

        ZoneVisit updated = service.confirm(1L);

        assertTrue(updated.isConfirmed());
    }
}
