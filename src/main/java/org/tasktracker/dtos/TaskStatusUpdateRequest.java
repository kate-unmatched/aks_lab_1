package org.tasktracker.dtos;


import org.tasktracker.entity.TaskStatus;

public class TaskStatusUpdateRequest {
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
