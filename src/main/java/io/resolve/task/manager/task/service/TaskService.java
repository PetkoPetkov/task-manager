package io.resolve.task.manager.task.service;

import io.resolve.task.manager.task.model.StatusTypeEnum;
import io.resolve.task.manager.task.model.TaskEntity;

import java.util.List;

public interface TaskService {

    TaskEntity create(TaskEntity task);

    void update(Long id, StatusTypeEnum status, Long userId);

    TaskEntity findById(Long id);

    List<TaskEntity> findAll();

    List<TaskEntity> findByUserId(Long userId);

    List<StatusTypeEnum> findAllTaskStatuses();
}
