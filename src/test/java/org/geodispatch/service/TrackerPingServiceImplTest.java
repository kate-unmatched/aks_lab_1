package org.geodispatch.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.service.ping.TrackerPingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TrackerPingServiceImplTest {

    private TrackerPingServiceImpl service;
    private EntityManager em;

    @BeforeEach
    void setup() {
        service = new TrackerPingServiceImpl();
        em = mock(EntityManager.class);
        service.em = em;
    }

    @Test
    void testFindAll() {
        TypedQuery<TrackerPing> query = mock(TypedQuery.class);

        when(em.createQuery(anyString(), eq(TrackerPing.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new TrackerPing()));

        List<TrackerPing> list = service.findAll();

        assertEquals(1, list.size());
        verify(query).getResultList();
    }

    @Test
    void testFindById() {
        TrackerPing ping = new TrackerPing();
        ping.setId(3L);

        when(em.find(TrackerPing.class, 3L)).thenReturn(ping);

        TrackerPing result = service.findById(3L);

        assertNotNull(result);
        assertEquals(3L, result.getId());
    }

    @Test
    void testCreate() {
        TrackerPing ping = new TrackerPing();
        ping.setTimestamp(Instant.now());

        when(em.merge(ping)).thenReturn(ping);

        TrackerPing created = service.create(ping);

        assertNotNull(created);
        verify(em).merge(ping);
    }

    @Test
    void testUpdate() {
        TrackerPing ping = new TrackerPing();
        when(em.merge(ping)).thenReturn(ping);

        TrackerPing updated = service.update(ping);

        assertNotNull(updated);
        verify(em).merge(ping);
    }
}
