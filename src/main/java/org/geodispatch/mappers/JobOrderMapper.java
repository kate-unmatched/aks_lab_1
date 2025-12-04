package org.geodispatch.mappers;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.geodispatch.dtos.JobOrderCreateRequestDTO;
import org.geodispatch.dtos.JobOrderResponseDTO;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.JobOrder;
import org.geodispatch.entity.Vehicle;

@Stateless
public class JobOrderMapper {

    @PersistenceContext
    private EntityManager em;

    public JobOrder fromCreateDto(JobOrderCreateRequestDTO dto) {
        JobOrder order = new JobOrder();
        order.setStatus(JobOrder.Status.valueOf(dto.getStatus()));

        if (dto.getVehicleId() != null) {
            Vehicle v = em.find(Vehicle.class, dto.getVehicleId());
            order.setVehicle(v);
        }

        if (dto.getPlannedZoneId() != null) {
            GeoZone zone = em.find(GeoZone.class, dto.getPlannedZoneId());
            order.setPlannedZone(zone);
        }

        return order;
    }

    public JobOrderResponseDTO toDto(JobOrder entity) {
        JobOrderResponseDTO dto = new JobOrderResponseDTO();

        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus().name());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStartedAt(entity.getStartedAt());
        dto.setCompletedAt(entity.getCompletedAt());

        if (entity.getVehicle() != null)
            dto.setVehicleId(entity.getVehicle().getId());

        if (entity.getPlannedZone() != null)
            dto.setPlannedZoneId(entity.getPlannedZone().getId());

        return dto;
    }
}
