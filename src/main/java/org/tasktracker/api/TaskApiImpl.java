package org.tasktracker.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.tasktracker.dtos.TaskStatusUpdateRequest;
import org.tasktracker.entity.TaskEntity;
import org.tasktracker.entity.TaskStatus;
import org.tasktracker.service.TaskService;

import java.util.Arrays;
import java.util.List;

public class TaskApiImpl implements TaskApi {

    @EJB
    private TaskService service;

    @Override
    public Response findAll(Long projectId) {
        List<TaskEntity> tasks = projectId == null
                ? service.findAll()
                : service.findByProject(projectId);

        return Response.ok(tasks).build();
    }

    @Override
    public Response findById(Long id) {
        TaskEntity task = service.findById(id);
        return task != null
                ? Response.ok(task).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response create(TaskEntity task) {
        TaskEntity created = service.create(task);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Override
    public Response updateStatus(Long id, TaskStatusUpdateRequest dto) {
        TaskEntity existing = service.findById(id);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Task not found")
                    .build();
        }

        existing.setStatus(dto.getStatus());
        TaskEntity updated = service.update(existing);

        return Response.ok(updated).build();
    }

    @Override
    public Response delete(Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @Override
    public Response getStatuses() {
        return Response.ok(Arrays.asList(TaskStatus.values())).build();
    }
}
