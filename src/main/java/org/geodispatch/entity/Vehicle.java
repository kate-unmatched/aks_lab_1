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
@Table(name = "vehicle",
        indexes = @Index(name = "idx_vehicle_plate", columnList = "registration_plate"))
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Column(name = "registration_plate", nullable = false, unique = true, length = 32)
    @ToString.Include
    private String registrationPlate;

    @Size(max = 128)
    @Column(length = 128)
    private String model;

    @Size(max = 128)
    @Column(length = 128)
    private String manufacturer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
