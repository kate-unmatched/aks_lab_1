package org.geodispatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "geo_zone",
        indexes = {
                @Index(name = "idx_zone_name", columnList = "name"),
                @Index(name = "idx_zone_lat_lon", columnList = "latitude, longitude")
        })
public class GeoZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotBlank
    @Size(max = 128)
    @Column(nullable = false, unique = true, length = 128)
    @ToString.Include
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Positive
    @Column(name = "radius_meters", nullable = false)
    private double radiusMeters;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
