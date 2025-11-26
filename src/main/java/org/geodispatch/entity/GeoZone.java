package org.geodispatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "geo_zone",
        indexes = {
                @Index(name = "idx_zone_name", columnList = "name"),
                @Index(name = "idx_zone_lat_lon", columnList = "latitude, longitude")
        })
public class GeoZone extends BaseEntity {

    @NotBlank
    @Size(max = 128)
    @Column(nullable = false, unique = true, length = 128)
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Positive
    @Column(name = "radius_meters", nullable = false)
    private double radiusMeters;
}


