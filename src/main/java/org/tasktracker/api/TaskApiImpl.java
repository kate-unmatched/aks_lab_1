package org.tasktracker.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.tasktracker.dtos.TaskAssigneeUpdateRequest;
import org.tasktracker.dtos.TaskCreateRequest;
import org.tasktracker.dtos.TaskStatusUpdateRequest;
import org.tasktracker.entity.TaskEntity;
import org.tasktracker.entity.TaskStatus;
import org.tasktracker.service.TaskService;

import java.util.Arrays;
import java.util.List;

public class TaskApiImpl implements TaskApi {

    @EJB
    private TaskService taskService;

    @Override
    public Response findAll(Long projectId) {
        List<TaskEntity> tasks = projectId == null
                ? taskService.findAll()
                : taskService.findByProject(projectId);

        return Response.ok(tasks).build();
    }

    @Override
    public Response findById(Long id) {
        TaskEntity task = taskService.findById(id);
        return task != null
                ? Response.ok(task).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response create(TaskCreateRequest task) {
        TaskEntity created = taskService.createTask(task);
        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    @Override
    public Response updateStatus(Long id, TaskStatusUpdateRequest req) {
        try {
            TaskEntity updated = taskService.updateStatus(id, req.status);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Override
    public Response delete(Long id) {
        taskService.delete(id);
        return Response.noContent().build();
    }

    @Override
    public Response getStatuses() {
        return Response.ok(Arrays.asList(TaskStatus.values())).build();
    }

    @Override
    public Response updateAssignee(Long id, TaskAssigneeUpdateRequest req) {
        try {
            TaskEntity updated = taskService.updateAssignee(id, req.assigneeId);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
