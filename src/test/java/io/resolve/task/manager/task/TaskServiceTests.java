package io.resolve.task.manager.task;

import io.resolve.task.manager.task.model.StatusTypeEnum;
import io.resolve.task.manager.task.model.TaskEntity;
import io.resolve.task.manager.task.repository.TaskRepository;
import io.resolve.task.manager.task.service.TaskServiceImpl;
import io.resolve.task.manager.task.validation.TaskValidator;
import io.resolve.task.manager.user.model.UserEntity;
import io.resolve.task.manager.user.validation.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    private static final Long ID = 1L;
    private static final String TITLE = "First task";
    private static final String DESCRIPTION = "Initial task";
    private static final LocalDate DUE_DATE = LocalDate.now();
    private static final StatusTypeEnum IN_PROGRESS = StatusTypeEnum.IN_PROGRESS;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskValidator taskValidator;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    void createTask_whenStatusDone_shouldCreateInProgress() {
        TaskEntity task = new TaskEntity(ID, TITLE, DESCRIPTION, DUE_DATE, StatusTypeEnum.DONE, null, null);

        Mockito.when(this.taskRepository.save(Mockito.any(TaskEntity.class))).thenReturn(task);
        Mockito.doNothing().when(this.taskValidator).validateTask(task);

        TaskEntity createdTask = this.taskService.create(task);

        Assertions.assertNotNull(createdTask);
        Assertions.assertNotNull(createdTask.getId());
        Assertions.assertEquals(ID, createdTask.getId());
        Assertions.assertEquals(IN_PROGRESS, createdTask.getStatus());
    }

    @Test
    void updateTask_withStatus_shouldUpdateTask() {
        TaskEntity existingTask = createTaskEntity();

        Mockito.when(this.taskValidator.validateAndGetTask(existingTask.getId())).thenReturn(existingTask);

        this.taskService.update(ID, StatusTypeEnum.DONE, null);

        Assertions.assertEquals(StatusTypeEnum.DONE, existingTask.getStatus());
    }

    @Test
    void updateTask_whenUserId_shouldUpdateTask() {
        TaskEntity task = createTaskEntity();
        UserEntity user = new UserEntity(ID, "Petko", "petkovpd@gmail.com", null);

        Mockito.when(this.taskValidator.validateAndGetTask(ID)).thenReturn(task);
        Mockito.when(this.userValidator.validateAndGetUserById(user.getId())).thenReturn(user);

        this.taskService.update(task.getId(), null, user.getId());

        Assertions.assertEquals(user.getId(), task.getUserId());
    }

    @Test
    void findById_withValidData() {
        TaskEntity task = createTaskEntity();

        Mockito.when(this.taskValidator.validateAndGetTask(task.getId())).thenReturn(task);

        TaskEntity foundTask = this.taskService.findById(task.getId());

        Assertions.assertNotNull(foundTask);
        Assertions.assertEquals(ID, foundTask.getId());
    }

    @Test
    void findAllTasks_withValidData() {
        List<TaskEntity> tasks = List.of(
                createTaskEntity(),
                new TaskEntity(2L, "Second task", DESCRIPTION, DUE_DATE, StatusTypeEnum.DONE, null, null)
        );

        Mockito.when(this.taskRepository.findAll()).thenReturn(tasks);

        List<TaskEntity> result = this.taskService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(TITLE, result.get(0).getTitle());
        Assertions.assertEquals("Second task", result.get(1).getTitle());

        Mockito.verify(this.taskRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findByUserId_withValidUser() {
        Long userId = 2L;
        List<TaskEntity> tasks = List.of(
                new TaskEntity(ID, TITLE, DESCRIPTION, DUE_DATE, IN_PROGRESS, userId, null),
                new TaskEntity(2L, "Second task", DESCRIPTION, DUE_DATE, StatusTypeEnum.DONE, userId, null)
        );

        Mockito.when(this.taskRepository.findByUserId(userId)).thenReturn(tasks);

        List<TaskEntity> userTasks = this.taskService.findByUserId(userId);

        Assertions.assertNotNull(userTasks);
        Assertions.assertEquals(2, userTasks.size());
        Assertions.assertEquals(TITLE, userTasks.get(0).getTitle());
        Assertions.assertEquals("Second task", userTasks.get(1).getTitle());
    }


    private TaskEntity createTaskEntity() {
        return new TaskEntity(ID, TITLE, DESCRIPTION, DUE_DATE, IN_PROGRESS, null, null);
    }
}
