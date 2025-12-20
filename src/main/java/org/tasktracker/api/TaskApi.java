package org.tasktracker.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.tasktracker.dtos.TaskAssigneeUpdateRequest;
import org.tasktracker.dtos.TaskCreateRequest;
import org.tasktracker.dtos.TaskStatusUpdateRequest;
import org.tasktracker.entity.TaskEntity;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TaskApi {

    @GET
    Response findAll(@QueryParam("projectId") Long projectId);

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") Long id);

    @POST
    Response create(TaskCreateRequest tas);

    @PUT
    @Path("/{id}/status")
    Response updateStatus(@PathParam("id") Long id, TaskStatusUpdateRequest dto);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);

    @GET
    @Path("/statuses")
    Response getStatuses();

    @PUT
    @Path("/{id}/assignee")
    Response updateAssignee(@PathParam("id") Long id, TaskAssigneeUpdateRequest request);

}
