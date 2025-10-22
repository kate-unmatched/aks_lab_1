package org.geodispatch.mappers;

import org.geodispatch.dtos.*;
import org.geodispatch.entity.*;
import org.geodispatch.utils.PageDto;

import java.util.List;
import java.util.function.Function;

public final class DtoMapper {
    private DtoMapper() {}

    public static <S,T> List<T> mapList(List<S> source, Function<S,T> mapper) {
        return source == null ? List.of() : source.stream().map(mapper).toList();
    }

    public static <E,D> PageDto<D> toPage(List<E> items, long total, int offset, int limit,
                                          Function<E,D> mapper) {
        return new PageDto<>(mapList(items, mapper), total, offset, limit);
    }

    public static ZoneDto toDto(GeoZone z) {
        return new ZoneDto(z.getId(), z.getName(), z.getLatitude(), z.getLongitude(),
                           z.getRadiusMeters(), z.getCreatedAt());
    }

    public static VehicleDto toDto(Vehicle v) {
        return new VehicleDto(v.getId(), v.getRegistrationPlate(), v.getModel(),
                              v.getManufacturer(), v.getCreatedAt());
    }

    public static OrderDto toDto(JobOrder o) {
        return new OrderDto(
                o.getId(),
                o.getVehicle() != null ? o.getVehicle().getId() : null,
                o.getStatus() != null ? o.getStatus().name() : null,
                o.getPlannedZone() != null ? o.getPlannedZone().getId() : null,
                o.getStartedAt(), o.getCompletedAt(), o.getCreatedAt()
        );
    }

    public static VisitDto toDto(ZoneVisit v) {
        return new VisitDto(
                v.getId(),
                v.getVehicle().getId(),
                v.getZone().getId(),
                v.getJobOrder() != null ? v.getJobOrder().getId() : null,
                v.getEnteredAt(), v.getLeftAt(), v.isConfirmed()
        );
    }

    public static PingResultDto.MatchedZone toMatched(GeoZone z, double distMeters) {
        return new PingResultDto.MatchedZone(z.getId(), z.getName(), distMeters);
    }
}
