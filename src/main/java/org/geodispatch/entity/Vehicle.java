package org.geodispatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicle",
        indexes = @Index(name = "idx_vehicle_plate", columnList = "registration_plate"))
public class Vehicle extends BaseEntity{

    @NotBlank
    @Size(max = 32)
    @Column(name = "registration_plate", nullable = false, unique = true, length = 32)
    private String registrationPlate;

    @Size(max = 128)
    @Column(length = 128)
    private String model;

    @Size(max = 128)
    @Column(length = 128)
    private String manufacturer;
}