package org.tasktracker.service;

import org.tasktracker.entity.BaseEntity;

import java.util.List;

public interface CrudService<T extends BaseEntity> {

    T create(T entity);

    T findById(Long id);

    List<T> findAll();

    T update(T entity);

    void delete(Long id);
}