package org.geodispatch.entity;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "tracker_ping",
        indexes = {
                @Index(name = "idx_ping_vehicle_time", columnList = "vehicle_id, timestamp"),
                @Index(name = "idx_ping_lat_lon", columnList = "latitude, longitude")
        })
public class TrackerPing extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @JsonbTransient
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "speed_kmh")
    private Double speedKmh;

    @Column(name = "heading_deg")
    private Double headingDegrees;

    @JsonbProperty("vehicleId")
    public Long getVehicleId() {
        return vehicle != null ? vehicle.getId() : null;
    }

    @JsonbProperty("vehicleId")
    public void setVehicleId(Long vehicleId) {
        if (vehicleId != null) {
            this.vehicle = new Vehicle();
            this.vehicle.setId(vehicleId);
        }
    }
}
