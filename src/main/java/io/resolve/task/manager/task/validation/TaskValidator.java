package io.resolve.task.manager.task.validation;

import io.resolve.task.manager.task.model.StatusTypeEnum;
import io.resolve.task.manager.task.model.TaskEntity;

public interface TaskValidator {

    void validateStatusAndUserId(StatusTypeEnum status, Long userId);

    TaskEntity validateAndGetTask(Long id);

    void validateTask(TaskEntity task);

    void validateDependsOn(TaskEntity task);
}
