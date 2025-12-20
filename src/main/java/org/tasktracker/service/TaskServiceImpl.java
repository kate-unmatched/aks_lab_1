package org.tasktracker.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.tasktracker.dtos.TaskCreateRequest;
import org.tasktracker.entity.ProjectEntity;
import org.tasktracker.entity.TaskEntity;
import org.tasktracker.entity.TaskStatus;
import org.tasktracker.entity.UserEntity;

import java.time.LocalDateTime;
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

    @Override
    public TaskEntity createTask(TaskCreateRequest req) {

        ProjectEntity project = em.find(ProjectEntity.class, req.projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        UserEntity assignee = em.find(UserEntity.class, req.assigneeId);
        if (assignee == null) {
            throw new IllegalArgumentException("User not found");
        }

        TaskEntity task = new TaskEntity();
        task.setTitle(req.title);
        task.setDescription(req.description);
        task.setProject(project);
        task.setAssignee(assignee);
        task.setStatus(TaskStatus.NEW);
        task.setCreatedAt(LocalDateTime.now());

        em.persist(task);
        return task;
    }

    @Override
    public TaskEntity updateStatus(Long taskId, TaskStatus status) {
        TaskEntity task = em.find(TaskEntity.class, taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }

        task.setStatus(status);
        return em.merge(task);
    }

    @Override
    public TaskEntity updateAssignee(Long taskId, Long assigneeId) {
        TaskEntity task = em.find(TaskEntity.class, taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }

        UserEntity assignee = em.find(UserEntity.class, assigneeId);
        if (assignee == null) {
            throw new IllegalArgumentException("User not found");
        }

        task.setAssignee(assignee);
        return em.merge(task);
    }

}
