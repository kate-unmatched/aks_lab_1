package org.geodispatch.ui;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.geodispatch.service.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class DashboardBean {

    @EJB
    private VehicleService vehicleService;

    @EJB
    private TrackerPingService pingService;

    @EJB
    private GeoZoneService zoneService;

    @EJB
    private ZoneVisitService visitService;

    @EJB
    private JobOrderService orderService;

    // Данные для UI
    private int activeVehiclesCount;
    private int lostVehiclesCount;
    private int activeZonesCount;
    private int incompleteVisitsCount;

    private List<String> recentEvents = new ArrayList<>();


    @PostConstruct
    public void init() {

        // === Активные машины ===
        activeVehiclesCount = vehicleService.findActiveVehicles().size();

        // === Пропавшие машины ===
        lostVehiclesCount = (int) vehicleService.findAll().stream()
                .filter(v -> {
                    var lastPing = pingService.findLastPing(v.getId());
                    return lastPing == null ||
                           Duration.between(lastPing.getCreatedAt(), LocalDateTime.now())
                                   .toMinutes() > 15;
                })
                .count();

        // === Активные зоны ===
        activeZonesCount = (int) zoneService.findAll().stream()
                .filter(z -> !zoneService.findVehiclesInsideZone(z.getId()).isEmpty())
                .count();

        // === Незавершённые визиты ===
        incompleteVisitsCount = (int) visitService.findAll().stream()
                .filter(v -> v.getLeftAt() == null)
                .count();

        // === Последние события (очень упрощённо) ===
        visitService.findAll().stream()
                .sorted((a, b) -> b.getEnteredAt().compareTo(a.getEnteredAt()))
                .limit(5)
                .forEach(v -> {
                    if (v.getLeftAt() == null)
                        recentEvents.add("Vehicle #" + v.getVehicle().getId() +
                                         " вошёл в Zone #" + v.getZone().getId());
                    else
                        recentEvents.add("Vehicle #" + v.getVehicle().getId() +
                                         " покинул Zone #" + v.getZone().getId());
                });
    }

    // === Геттеры ===

    public int getActiveVehiclesCount() {
        return activeVehiclesCount;
    }

    public int getLostVehiclesCount() {
        return lostVehiclesCount;
    }

    public int getActiveZonesCount() {
        return activeZonesCount;
    }

    public int getIncompleteVisitsCount() {
        return incompleteVisitsCount;
    }

    public List<String> getRecentEvents() {
        return recentEvents;
    }
}
