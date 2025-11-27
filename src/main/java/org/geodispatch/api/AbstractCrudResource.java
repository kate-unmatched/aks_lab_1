package org.geodispatch.api;

import jakarta.ws.rs.core.Response;
import org.geodispatch.entity.BaseEntity;
import org.geodispatch.services.base.CrudService;


import java.util.List;

public abstract class AbstractCrudResource<T extends BaseEntity>
        implements CrudResource<T> {

    protected abstract CrudService<T> getService();

    @Override
    public List<T> getAll() {
        return getService().findAll();
    }

    @Override
    public Response getById(Long id) {
        T entity = getService().findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Entity not found")
                           .build();
        }
        return Response.ok(entity).build();
    }

    @Override
    public Response create(T entity) {
        getService().create(entity);
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @Override
    public Response update(Long id, T newData) {
        T entity = getService().findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Entity not found")
                           .build();
        }

        applyUpdates(entity, newData);
        entity = getService().update(entity);

        return Response.ok(entity).build();
    }

    @Override
    public Response delete(Long id) {
        T entity = getService().findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Entity not found")
                           .build();
        }

        getService().delete(id);
        return Response.noContent().build();
    }

    /**
     * Абстрактный метод для копирования данных сущности при update().
     */
    protected abstract void applyUpdates(T existing, T newData);
}
