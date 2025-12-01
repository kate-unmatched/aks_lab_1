package org.geodispatch.entity;

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
@Table(name = "job_order",
        indexes = {
                @Index(name = "idx_order_vehicle", columnList = "vehicle_id"),
                @Index(name = "idx_order_status", columnList = "status")
        })
public class JobOrder extends BaseEntity {

    public enum Status { PLANNED, IN_PROGRESS, COMPLETED }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Status status = Status.PLANNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planned_zone_id")
    private GeoZone plannedZone;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
