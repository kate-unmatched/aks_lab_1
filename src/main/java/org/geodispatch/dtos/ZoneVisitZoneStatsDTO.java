package org.geodispatch.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ZoneVisitZoneStatsDTO {

    private Long zoneId;

    private long totalVisits;
    private long uniqueVehicles;
    private long incompleteVisits; // leftAt = null
    private Double avgDurationMinutes;
}
