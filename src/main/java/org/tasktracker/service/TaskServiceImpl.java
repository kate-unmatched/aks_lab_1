package org.tasktracker.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.tasktracker.entity.TaskEntity;

import java.util.List;

@Stateless
public class TaskServiceImpl extends CrudServiceImpl<TaskEntity> implements TaskService {

    public TaskServiceImpl() {
        super(TaskEntity.class);
    }

    @Override
    public List<TaskEntity> findByProject(Long projectId) {
        TypedQuery<TaskEntity> query = em.createQuery(
                "SELECT t FROM TaskEntity t WHERE t.project.id = :projectId",
                TaskEntity.class
        );
        query.setParameter("projectId", projectId);
        return query.getResultList();
    }
}
