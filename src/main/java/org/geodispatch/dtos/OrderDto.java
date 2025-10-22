package org.geodispatch.dtos;

import java.time.Instant;

public record OrderDto(
        Long id,
        Long vehicleId,
        String status,
        Long plannedZoneId,
        Instant startedAt,
        Instant completedAt,
        Instant createdAt
) { }
