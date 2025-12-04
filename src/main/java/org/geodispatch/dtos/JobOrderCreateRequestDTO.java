package org.geodispatch.dtos;

import lombok.Data;

@Data
public class JobOrderCreateRequestDTO {
    private Long vehicleId;
    private Long plannedZoneId;
    private String status;
}