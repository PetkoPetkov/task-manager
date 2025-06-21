package io.resolve.task.manager.task.mapper;

import io.resolve.task.manager.task.TaskDto;
import io.resolve.task.manager.task.model.TaskEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TaskMapper {

    TaskDto toDto(TaskEntity entity);

    TaskEntity fromDto(TaskDto dto);
}
