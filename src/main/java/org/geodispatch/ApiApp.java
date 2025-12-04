package org.geodispatch;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class ApiApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new HashSet<>();

        resources.add(org.geodispatch.api.VehicleApiImpl.class);
        resources.add(org.geodispatch.api.TrackerPingApiImpl.class);
        resources.add(org.geodispatch.api.GeoZoneApiImpl.class);
        resources.add(org.geodispatch.api.JobOrderApiImpl.class);
        resources.add(org.geodispatch.api.ZoneVisitApiImpl.class);

        return resources;
    }
}
