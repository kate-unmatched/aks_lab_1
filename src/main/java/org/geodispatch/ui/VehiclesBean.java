package org.geodispatch.ui;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.TrackerPingService;
import org.geodispatch.service.VehicleService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Named
@RequestScoped
public class VehiclesBean {

    @EJB
    private VehicleService vehicleService;

    @EJB
    private TrackerPingService pingService;

    private List<Vehicle> allVehicles;

    @PostConstruct
    public void init() {
        allVehicles = vehicleService.findAll();
    }

    public List<Vehicle> getAllVehicles() {
        return allVehicles;
    }

    public String lastPingText(Long id) {
        var ping = pingService.findLastPing(id);
        if (ping == null) return "нет данных";

        Duration diff = Duration.between(ping.getCreatedAt(), LocalDateTime.now());

        return diff.toMinutes() < 15
                ? "OK (" + diff.toMinutes() + " мин назад)"
                : "Потерян (" + diff.toMinutes() + " мин назад)";
    }
}
