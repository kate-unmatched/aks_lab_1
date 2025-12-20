package org.tasktracker.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.tasktracker.entity.ProjectEntity;
import org.tasktracker.service.ProjectService;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectApiImpl implements ProjectApi {

    @EJB
    private ProjectService service;

    @Override
    public Response findAll() {
        List<ProjectEntity> projects = service.findAll();
        return Response.ok(projects).build();
    }

    @Override
    public Response findById(Long id) {
        ProjectEntity project = service.findById(id);
        return project != null
                ? Response.ok(project).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response create(ProjectEntity project) {
        project.setCreatedAt(LocalDateTime.now());
        ProjectEntity created = service.create(project);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Override
    public Response update(Long id, ProjectEntity incoming) {
        ProjectEntity existing = service.findById(id);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Project not found")
                    .build();
        }

        existing.setName(incoming.getName());
        existing.setDescription(incoming.getDescription());

        ProjectEntity updated = service.update(existing);
        return Response.ok(updated).build();
    }

    @Override
    public Response delete(Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
