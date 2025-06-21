package io.resolve.task.manager.task.service;

import io.resolve.task.manager.task.model.StatusTypeEnum;
import io.resolve.task.manager.task.model.TaskEntity;
import io.resolve.task.manager.task.repository.TaskRepository;
import io.resolve.task.manager.task.validation.TaskValidator;
import io.resolve.task.manager.user.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;
    private final UserValidator userValidator;

    @Autowired
    public TaskServiceImpl(final TaskRepository taskRepository,
                           final TaskValidator taskValidator,
                           final UserValidator userValidator) {
        this.taskRepository = taskRepository;
        this.taskValidator = taskValidator;
        this.userValidator = userValidator;
    }


    @Override
    @Transactional
    public TaskEntity create(TaskEntity task) {
        this.taskValidator.validateTask(task);

        task.setStatus(StatusTypeEnum.IN_PROGRESS);

        return this.taskRepository.save(task);
    }

    @Override
    @Transactional
    public void update(Long id, StatusTypeEnum status, Long userId) {
        this.taskValidator.validateStatusAndUserId(status, userId);

        TaskEntity task = this.taskValidator.validateAndGetTask(id);
        if (status != null) {
            this.taskValidator.validateDependsOn(task);
            task.setStatus(status);
        } else if (userId != null && (this.userValidator.validateAndGetUserById(userId) != null)) {
            task.setUserId(userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TaskEntity findById(Long id) {
        return this.taskValidator.validateAndGetTask(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskEntity> findAll() {
        return this.taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskEntity> findByUserId(Long userId) {
        return this.taskRepository.findByUserId(userId);
    }
}
