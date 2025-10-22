package org.geodispatch.dtos;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record VehicleDto(
        Long id,
        @NotBlank @Size(max=32) String registrationPlate,
        @Size(max=128) String model,
        @Size(max=128) String manufacturer,
        Instant createdAt
) { }
