package org.geodispatch.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface CrudResource<T> {

    @GET
    List<T> getAll();

    @GET
    @Path("/{id}")
    Response getById(@PathParam("id") Long id);

    @POST
    Response create(T entity);

    @PUT
    @Path("/{id}")
    Response update(@PathParam("id") Long id, T entity);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") Long id);
}
