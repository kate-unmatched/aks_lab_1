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
@Table(name = "zone_visit",
        indexes = {
                @Index(name = "idx_visit_vehicle_zone", columnList = "vehicle_id, zone_id"),
                @Index(name = "idx_visit_order", columnList = "job_order_id")
        })
public class ZoneVisit extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GeoZone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_order_id")
    private JobOrder jobOrder;

    @Column(name = "entered_at")
    private Instant enteredAt;

    @Column(name = "left_at")
    private Instant leftAt;

    @Column(nullable = false)
    private boolean confirmed = false;
}
