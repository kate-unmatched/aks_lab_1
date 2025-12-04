package org.geodispatch.dtos;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ZoneVisitDTO {
    private Long id;
    private Long vehicleId;
    private Long zoneId;
    private Long jobOrderId;
    private LocalDateTime enteredAt;
    private LocalDateTime leftAt;
    private boolean confirmed;
}
