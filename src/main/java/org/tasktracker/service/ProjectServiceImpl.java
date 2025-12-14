package org.tasktracker.service;

import jakarta.ejb.Stateless;
import org.tasktracker.entity.ProjectEntity;

@Stateless
public class ProjectServiceImpl
        extends CrudServiceImpl<ProjectEntity>
        implements ProjectService {

    public ProjectServiceImpl() {
        super(ProjectEntity.class);
    }
}
