package org.geodispatch.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobOrderResponseDTO {

    private Long id;
    private String status;

    private Long vehicleId;
    private Long plannedZoneId;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}