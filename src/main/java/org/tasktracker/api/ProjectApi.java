package org.tasktracker.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.tasktracker.entity.ProjectEntity;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProjectApi {

    @GET
    Response findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") Long id);

    @POST
    Response create(ProjectEntity project);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);
}
