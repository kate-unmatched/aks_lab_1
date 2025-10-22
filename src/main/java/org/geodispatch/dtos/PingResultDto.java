package org.geodispatch.dtos;

import java.util.List;

public record PingResultDto(
        Long pingId,
        List<MatchedZone> matchedZones,
        Boolean orderCompleted
) {
    public record MatchedZone(Long zoneId, String name, double distanceMeters) {}
}
