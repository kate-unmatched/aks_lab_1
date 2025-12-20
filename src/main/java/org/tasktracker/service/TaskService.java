package org.tasktracker.service;

import org.tasktracker.dtos.TaskCreateRequest;
import org.tasktracker.entity.TaskEntity;
import org.tasktracker.entity.TaskStatus;

import java.util.List;

public interface TaskService extends CrudService<TaskEntity> {

    List<TaskEntity> findByProject(Long projectId);

    TaskEntity createTask(TaskCreateRequest req);

    TaskEntity updateStatus(Long taskId, TaskStatus status);

    TaskEntity updateAssignee(Long taskId, Long assigneeId);

}
