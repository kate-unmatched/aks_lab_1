package org.geodispatch.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ZoneStatsDTO {

    private Long zoneId;
    private long totalVisits;
    private long uniqueVehicles;
}
