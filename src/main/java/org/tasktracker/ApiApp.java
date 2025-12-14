package org.tasktracker;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class ApiApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new HashSet<>();
        resources.add(org.tasktracker.api.ProjectApiImpl.class);
        resources.add(org.tasktracker.api.TaskApiImpl.class);
        resources.add(org.tasktracker.api.UserApiImpl.class);
        return resources;
    }
}
