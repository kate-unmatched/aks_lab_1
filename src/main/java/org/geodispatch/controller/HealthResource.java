package org.geodispatch.controller;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @PersistenceContext(unitName = "geoPU")
    EntityManager em;

    @GET
    public String health() {
        em.createNativeQuery("select 1").getSingleResult();
        return "{\"status\":\"ok\"}";
    }
}
