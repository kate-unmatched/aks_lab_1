package org.geodispatch.dtos;

import java.time.Instant;

public record VisitDto(
        Long id,
        Long vehicleId,
        Long zoneId,
        Long jobOrderId,
        Instant enteredAt,
        Instant leftAt,
        boolean confirmed
) { }
