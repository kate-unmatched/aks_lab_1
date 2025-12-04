package org.geodispatch.mappers;

import jakarta.ejb.Stateless;
import org.geodispatch.dtos.ZoneVisitDTO;
import org.geodispatch.dtos.ZoneVisitResponseDTO;
import org.geodispatch.entity.ZoneVisit;

@Stateless
public class ZoneVisitMapper {

    public ZoneVisitDTO toDto(ZoneVisit v) {
        ZoneVisitDTO dto = new ZoneVisitDTO();
        dto.setId(v.getId());
        dto.setEnteredAt(v.getEnteredAt());
        dto.setLeftAt(v.getLeftAt());
        dto.setConfirmed(v.isConfirmed());

        if (v.getVehicle() != null)
            dto.setVehicleId(v.getVehicle().getId());

        if (v.getZone() != null)
            dto.setZoneId(v.getZone().getId());

        if (v.getJobOrder() != null)
            dto.setJobOrderId(v.getJobOrder().getId());

        return dto;
    }


    public ZoneVisitResponseDTO toResponseDto(ZoneVisit v) {
        ZoneVisitResponseDTO dto = new ZoneVisitResponseDTO();

        dto.setId(v.getId());
        dto.setEnteredAt(v.getEnteredAt());
        dto.setLeftAt(v.getLeftAt());
        dto.setConfirmed(v.isConfirmed());
        dto.setCreatedAt(v.getCreatedAt());

        if (v.getVehicle() != null)
            dto.setVehicleId(v.getVehicle().getId());

        if (v.getZone() != null)
            dto.setZoneId(v.getZone().getId());

        if (v.getJobOrder() != null)
            dto.setJobOrderId(v.getJobOrder().getId());

        return dto;
    }
}
