package org.geodispatch.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.geodispatch.api.GeoZoneApi;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.GeoZoneService;
import org.geodispatch.service.ZoneVisitService;

import java.util.List;

public class GeoZoneApiImpl implements GeoZoneApi {

    @EJB
    private GeoZoneService service;

    @EJB
    private ZoneVisitService visitService;

    @Override
    public Response findAll() {
        return Response.ok(service.findAll()).build();
    }

    @Override
    public Response findById(Long id) {
        GeoZone zone = service.findById(id);
        return zone != null
                ? Response.ok(zone).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response create(GeoZone zone) {
        GeoZone created = service.create(zone);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Override
    public Response delete(Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @Override
    public Response contains(Long id, Double lat, Double lon) {
        if (lat == null || lon == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("lat and lon are required")
                    .build();
        }

        boolean inside = service.contains(id, lat, lon);
        return Response.ok(inside).build();
    }

    @Override
    public Response vehiclesInside(Long id) {
        List<Vehicle> vehicles = service.findVehiclesInsideZone(id);
        return Response.ok(vehicles).build();
    }

    @Override
    public Response getAllZonesStats() {
        var statsList = visitService.getAllZonesStats();
        return Response.ok(statsList).build();
    }
}
