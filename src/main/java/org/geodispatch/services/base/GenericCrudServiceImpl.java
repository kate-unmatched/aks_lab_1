package org.geodispatch.services.base;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.geodispatch.entity.BaseEntity;

import java.util.List;

public abstract class GenericCrudServiceImpl<T extends BaseEntity> implements CrudService<T> {

    @PersistenceContext(unitName = "geoPU")
    protected EntityManager em;

    private final Class<T> entityClass;

    protected GenericCrudServiceImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T create(T entity) {
        em.persist(entity);
        return entity;
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Long id) {
        T entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }
}
