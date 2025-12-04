package org.geodispatch.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class ZoneVisitUpdateRequestDTO {
    private LocalDateTime leftAt;
    private Boolean confirmed;
}
