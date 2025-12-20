package org.tasktracker.dtos;

import lombok.Data;

@Data
public class TaskCreateRequest {
    public String title;
    public String description;
    public Long projectId;
    public Long assigneeId;
}
