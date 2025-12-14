package org.tasktracker.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.tasktracker.entity.UserEntity;
import org.tasktracker.service.UserService;

import java.util.List;

public class UserApiImpl implements UserApi {

    @EJB
    private UserService service;

    @Override
    public Response findAll() {
        List<UserEntity> users = service.findAll();
        return Response.ok(users).build();
    }

    @Override
    public Response findById(Long id) {
        UserEntity user = service.findById(id);
        return user != null
                ? Response.ok(user).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response create(UserEntity user) {
        UserEntity created = service.create(user);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Override
    public Response delete(Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
