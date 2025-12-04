package org.geodispatch.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.geodispatch.api.TrackerPingApi;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.service.TrackerPingService;

import java.util.List;

public class TrackerPingApiImpl implements TrackerPingApi {

    @EJB
    private TrackerPingService service;

    @Override
    public Response findAll() {
        return Response.ok(service.findAll()).build();
    }

    @Override
    public Response findById(Long id) {
        TrackerPing ping = service.findById(id);
        return ping != null
                ? Response.ok(ping).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response findByVehicle(Long vehicleId) {
        List<TrackerPing> list = service.findByVehicle(vehicleId);
        return Response.ok(list).build();
    }

    @Override
    public Response lastPing(Long vehicleId) {
        TrackerPing ping = service.findLastPing(vehicleId);
        return ping != null
                ? Response.ok(ping).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response create(TrackerPing ping) {
        TrackerPing created = service.create(ping);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Override
    public Response delete(Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
