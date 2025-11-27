package org.geodispatch.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.service.zone.GeoZoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeoZoneServiceImplTest {

    private GeoZoneServiceImpl service;

    @BeforeEach
    void setup() {
        service = new GeoZoneServiceImpl();
        service.em = mock(EntityManager.class);
    }

    /**
     * Verifies that a point located inside the zone's radius
     * is correctly recognized and returned in the result list.
     *
     * <p>The test uses:
     * - a single zone with known coordinates
     * - a test point located ~250m away
     */
    @Test
    void testFindContainingPoint() {
        GeoZone zone = new GeoZone();
        zone.setName("Center");
        zone.setLatitude(50.0);
        zone.setLongitude(50.0);
        zone.setRadiusMeters(500);

        TypedQuery<GeoZone> query = mock(TypedQuery.class);
        when(service.em.createQuery(anyString(), eq(GeoZone.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(zone));

        // ==== Вызываем тестируемый метод ====
        List<GeoZone> result = service.findContainingPoint(50.002, 50.002);

        assertEquals(1, result.size());
        assertEquals("Center", result.get(0).getName());
    }
}
