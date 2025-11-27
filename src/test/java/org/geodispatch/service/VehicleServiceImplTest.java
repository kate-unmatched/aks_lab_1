package org.geodispatch.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.vehicle.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VehicleServiceImpl}.
 *
 * <p>Verifies CRUD operations:
 * - findAll() returns list of vehicles
 * - findById() returns expected vehicle
 * - create() delegates to EntityManager
 * - update() merges entity
 * - delete() removes entity
 *
 * <p>All tests mock EntityManager to avoid real DB interaction.
 */
class VehicleServiceImplTest {

    private VehicleServiceImpl service;
    private EntityManager em;

    @BeforeEach
    void setup() {
        service = new VehicleServiceImpl();
        em = mock(EntityManager.class);
        service.em = em;
    }

    @Test
    void testFindAll() {
        TypedQuery<Vehicle> query = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Vehicle.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Vehicle()));

        List<Vehicle> list = service.findAll();

        assertEquals(1, list.size());
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindById() {
        Vehicle v = new Vehicle();
        v.setId(10L);

        when(em.find(Vehicle.class, 10L)).thenReturn(v);

        Vehicle result = service.findById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void testCreate() {
        Vehicle v = new Vehicle();
        when(em.merge(v)).thenReturn(v);

        Vehicle created = service.create(v);

        assertNotNull(created);
        verify(em).merge(v);
    }

    @Test
    void testUpdate() {
        Vehicle v = new Vehicle();
        when(em.merge(v)).thenReturn(v);

        Vehicle updated = service.update(v);

        assertNotNull(updated);
        verify(em).merge(v);
    }

    @Test
    void testDelete() {
        Vehicle v = new Vehicle();
        v.setId(5L);

        when(em.find(Vehicle.class, 5L)).thenReturn(v);

        service.delete(5L);

        verify(em).remove(v);
    }
}
