package org.tasktracker.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "projects", schema = "tasktracker")
public class ProjectEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonbTransient
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TaskEntity> tasks;

}
