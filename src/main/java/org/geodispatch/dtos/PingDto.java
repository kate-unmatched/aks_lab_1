package org.geodispatch.dtos;

import jakarta.validation.constraints.*;
public record PingDto(
        @NotNull Long vehicleId,
        @NotBlank String timestamp,  // ISO-8601
        @NotNull Double latitude,
        @NotNull Double longitude,
        Double speedKmh,
        Double headingDegrees
) { }