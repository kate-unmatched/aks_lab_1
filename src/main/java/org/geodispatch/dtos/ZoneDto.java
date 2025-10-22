package org.geodispatch.dtos;

import jakarta.validation.constraints.*;

import java.time.Instant;

public record ZoneDto(
        Long id,
        @NotBlank @Size(max=128) String name,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @Positive Double radiusMeters,
        Instant createdAt
) { }
