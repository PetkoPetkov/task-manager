package io.resolve.task.manager.task;

import io.resolve.task.manager.exception.TaskManagerException;
import io.resolve.task.manager.task.model.StatusTypeEnum;
import io.resolve.task.manager.task.model.TaskEntity;
import io.resolve.task.manager.task.repository.TaskRepository;
import io.resolve.task.manager.task.validation.TaskValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class TaskValidatorTests {

    private static final Long ID = 1L;
    private static final String TITLE = "First task";
    private static final String DESCRIPTION = "Initial task";
    private static final LocalDate DUE_DATE = LocalDate.now();
    private static final StatusTypeEnum STATUS = StatusTypeEnum.IN_PROGRESS;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskValidatorImpl taskValidator;


    @Test
    void validateStatusAndUserId_whenValidData() {
        Assertions.assertDoesNotThrow(() -> this.taskValidator.validateStatusAndUserId(STATUS, ID));
    }

    @Test
    void validateStatusAndUserId_whenStatusExists() {
        Assertions.assertDoesNotThrow(() -> this.taskValidator.validateStatusAndUserId(STATUS, null));
    }

    @Test
    void validateStatusAndUserId_whenUserIdExists() {
        Assertions.assertDoesNotThrow(() -> this.taskValidator.validateStatusAndUserId(null, ID));
    }

    @Test
    void validateStatusAndUserId_whenStatusAndUserIdNotExists() {
        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.taskValidator.validateStatusAndUserId(null, null));

        Assertions.assertEquals("You must provide status or user id", ex.getMessage());
    }

    @Test
    void validateAndGetTask_whenTaskExists() {
        TaskEntity task = new TaskEntity(ID, TITLE, DESCRIPTION, DUE_DATE, STATUS, null, null);

        Mockito.when(this.taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Assertions.assertDoesNotThrow(() -> this.taskValidator.validateAndGetTask(task.getId()));
    }

    @Test
    void validateAndGetTask_whenTaskNotExists() {
        Long taskId = 2L;
        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.taskValidator.validateAndGetTask(taskId));

        Assertions.assertEquals("There is no task with id: " + taskId, ex.getMessage());
    }

    @Test
    void validateTask_whenTitleNotExists() {
        TaskEntity task = new TaskEntity(ID, null, DESCRIPTION, DUE_DATE, STATUS, null, null);

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.taskValidator.validateTask(task));

        Assertions.assertEquals("Task title is missing", ex.getMessage());
    }

    @Test
    void validateTask_whenDueDateNotExists() {
        TaskEntity task = new TaskEntity(ID, TITLE, DESCRIPTION, null, STATUS, null, null);

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.taskValidator.validateTask(task));

        Assertions.assertEquals("Task due date is missing", ex.getMessage());
    }

    @Test
    void validateTask_whenDependsOnNotExists() {
        Long dependsOn = 1L;
        TaskEntity task = new TaskEntity(ID, TITLE, DESCRIPTION, DUE_DATE, STATUS, null, dependsOn);

        Mockito.when(this.taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.taskValidator.validateTask(task));

        Assertions.assertEquals("There is no task with id: " + task.getId(), ex.getMessage());
    }

    @Test
    void validateDependsOn_whenDependsOnNotCompleted() {
        Long dependsOnId = 2L;
        TaskEntity task = new TaskEntity(ID, TITLE, DESCRIPTION, DUE_DATE, STATUS, null, dependsOnId);
        TaskEntity dependentTask = new TaskEntity(dependsOnId, TITLE, DESCRIPTION, DUE_DATE, STATUS, null, null);

        Mockito.when(this.taskRepository.findById(task.getDependsOn())).thenReturn(Optional.of(dependentTask));

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.taskValidator.validateDependsOn(task));

        Assertions.assertEquals("The task depends on another task which is not completed", ex.getMessage());
    }

}
