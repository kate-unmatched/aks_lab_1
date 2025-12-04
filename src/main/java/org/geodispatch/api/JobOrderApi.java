package org.geodispatch.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.geodispatch.dtos.JobOrderCreateRequestDTO;
import org.geodispatch.dtos.JobOrderUpdateRequestDTO;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface JobOrderApi {

    @POST
    Response create(JobOrderCreateRequestDTO req);

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") Long id);

    @PUT
    @Path("/{id}")
    Response update(@PathParam("id") Long id, JobOrderUpdateRequestDTO req);

    @GET
    Response findAll();
}
