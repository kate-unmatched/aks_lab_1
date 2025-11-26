package org.geodispatch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "speed_kmh")
    private Double speedKmh;

    @Column(name = "heading_deg")
    private Double headingDegrees;
}
