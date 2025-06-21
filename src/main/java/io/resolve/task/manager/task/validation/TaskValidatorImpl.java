package io.resolve.task.manager.task.validation;

import io.resolve.task.manager.exception.TaskManagerException;
import io.resolve.task.manager.task.model.StatusTypeEnum;
import io.resolve.task.manager.task.model.TaskEntity;
import io.resolve.task.manager.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskValidatorImpl implements TaskValidator {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskValidatorImpl(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void validateStatusAndUserId(StatusTypeEnum status, Long userId) {
        if (status == null && userId == null) {
            throw new TaskManagerException("You must provide status or user id");
        }
    }

    @Override
    public TaskEntity validateAndGetTask(Long id) {
        return this.taskRepository.findById(id).orElseThrow(
                () -> new TaskManagerException("There is no task with id: " + id));
    }

    @Override
    public void validateTask(TaskEntity task) {
        if (task.getTitle() == null) {
            throw new TaskManagerException("Task title is missing");
        }
        if (task.getDueDate() == null) {
            throw new TaskManagerException("Task due date is missing");
        }
        if (task.getDependsOn() != null) {
            validateAndGetTask(task.getDependsOn());
        }
    }

    @Override
    public void validateDependsOn(TaskEntity task) {
        Long dependsOnId = task.getDependsOn();
        if (dependsOnId != null) {
            Optional<TaskEntity> dependsOn = this.taskRepository.findById(dependsOnId);
            if (dependsOn.isPresent() && (dependsOn.get().getStatus() != StatusTypeEnum.DONE)) {
                throw new TaskManagerException("The task depends on another task which is not completed");
            }
        }
    }

}
