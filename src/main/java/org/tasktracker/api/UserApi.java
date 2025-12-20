package org.tasktracker.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.tasktracker.entity.UserEntity;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserApi {

    @GET
    Response findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") Long id);

    @POST
    Response create(UserEntity user);

    @PUT
    @Path("/{id}")
    Response update(@PathParam("id") Long id, UserEntity user);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);
}
