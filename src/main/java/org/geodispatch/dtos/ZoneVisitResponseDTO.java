package org.geodispatch.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ZoneVisitResponseDTO {

    private Long id;
    private Long zoneId;
    private Long vehicleId;
    private Long jobOrderId;

    private LocalDateTime enteredAt;
    private LocalDateTime leftAt;

    private boolean confirmed;
    private LocalDateTime createdAt;
}
