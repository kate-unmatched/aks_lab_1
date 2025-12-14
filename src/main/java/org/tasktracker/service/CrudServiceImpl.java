package org.tasktracker.service;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.tasktracker.entity.BaseEntity;

import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class CrudServiceImpl<T extends BaseEntity> implements CrudService<T> {

    @PersistenceContext(unitName = "taskTrackerPU")
    public EntityManager em;

    private final Class<T> entityClass;

    protected CrudServiceImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T create(T entity) {
        return em.merge(entity);
    }

    @Override
    public T findById(Long id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    @Override
    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(Long id) {
        T entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }
}
