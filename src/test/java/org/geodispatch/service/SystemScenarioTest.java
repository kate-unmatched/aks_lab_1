package org.geodispatch.service;

import org.geodispatch.entity.*;
import org.geodispatch.service.order.JobOrderService;
import org.geodispatch.service.ping.TrackerPingService;
import org.geodispatch.service.vehicle.VehicleService;
import org.geodispatch.service.visit.ZoneVisitService;
import org.geodispatch.service.zone.GeoZoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * High-level scenario test that verifies interaction of several services together.
 *
 * <p>This test simulates a full business case:
 * 1. Create vehicle and zone
 * 2. Create job order
 * 3. Start the order
 * 4. Receive tracker pings
 * 5. Detect zone entry
 * 6. Leave zone
 * 7. Complete the order
 *
 * <p>All operations are mocked to isolate scenario logic from persistence.
 */
class SystemScenarioTest {

    private VehicleService vehicleService;
    private GeoZoneService geoZoneService;
    private JobOrderService jobOrderService;
    private TrackerPingService pingService;
    private ZoneVisitService visitService;

    @BeforeEach
    void setup() {
        vehicleService = mock(VehicleService.class);
        geoZoneService = mock(GeoZoneService.class);
        jobOrderService = mock(JobOrderService.class);
        pingService = mock(TrackerPingService.class);
        visitService = mock(ZoneVisitService.class);
    }

    @Test
    void fullWorkflowScenario() {
        // 1. Create vehicle
        Vehicle car = new Vehicle();
        car.setId(1L);
        when(vehicleService.findById(1L)).thenReturn(car);

        // 2. Create zone
        GeoZone zone = new GeoZone();
        zone.setId(10L);
        zone.setName("Depot");
        zone.setLatitude(50.0);
        zone.setLongitude(50.0);
        zone.setRadiusMeters(300);

        when(geoZoneService.findAll()).thenReturn(List.of(zone));

        // 3. Create job order
        JobOrder order = new JobOrder();
        order.setId(100L);
        order.setVehicle(car);
        order.setStatus(JobOrder.Status.PLANNED);

        when(jobOrderService.findById(100L)).thenReturn(order);

        // 4. Start the order
        JobOrder started = new JobOrder();
        started.setId(100L);
        started.setStatus(JobOrder.Status.IN_PROGRESS);
        started.setStartedAt(Instant.now());

        when(jobOrderService.startOrder(100L)).thenReturn(started);

        JobOrder afterStart = jobOrderService.startOrder(100L);
        assertEquals(JobOrder.Status.IN_PROGRESS, afterStart.getStatus());

        // 5. Receive tracker ping
        TrackerPing ping = new TrackerPing();
        ping.setVehicle(car);
        ping.setLatitude(50.001);
        ping.setLongitude(50.001);
        ping.setTimestamp(Instant.now());

        when(pingService.create(any())).thenReturn(ping);

        TrackerPing savedPing = pingService.create(ping);
        assertNotNull(savedPing);

        // 6. Detect zone entry
        when(geoZoneService.findContainingPoint(
                savedPing.getLatitude(), savedPing.getLongitude()
        )).thenReturn(List.of(zone));

        List<GeoZone> zones = geoZoneService.findContainingPoint(
                savedPing.getLatitude(),
                savedPing.getLongitude()
        );
        assertEquals(1, zones.size());

        // 7. Create visit
        ZoneVisit visit = new ZoneVisit();
        visit.setId(500L);
        visit.setVehicle(car);
        visit.setZone(zone);
        visit.setEnteredAt(Instant.now());

        when(visitService.enter(eq(500L), any())).thenReturn(visit);

        ZoneVisit entered = visitService.enter(500L, Instant.now());
        assertNotNull(entered.getEnteredAt());

        // 8. Leave zone
        when(visitService.leave(eq(500L), any())).then(inv -> {
            visit.setLeftAt(Instant.now());
            return visit;
        });

        ZoneVisit left = visitService.leave(500L, Instant.now());
        assertNotNull(left.getLeftAt());

        // 9. Complete order
        JobOrder completed = new JobOrder();
        completed.setId(100L);
        completed.setStatus(JobOrder.Status.COMPLETED);
        completed.setCompletedAt(Instant.now());

        when(jobOrderService.completeOrder(100L)).thenReturn(completed);

        JobOrder afterComplete = jobOrderService.completeOrder(100L);
        assertEquals(JobOrder.Status.COMPLETED, afterComplete.getStatus());
        assertNotNull(afterComplete.getCompletedAt());
    }
}
