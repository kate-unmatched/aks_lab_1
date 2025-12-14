package org.tasktracker.service;

import org.tasktracker.entity.TaskEntity;

import java.util.List;

public interface TaskService extends CrudService<TaskEntity> {

    List<TaskEntity> findByProject(Long projectId);

}
