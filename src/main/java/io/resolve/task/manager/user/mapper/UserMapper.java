package io.resolve.task.manager.user.mapper;

import io.resolve.task.manager.user.dto.UserDto;
import io.resolve.task.manager.user.model.UserEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {

    UserDto toDto(UserEntity entity);

    @Mapping(target = "tasks", ignore = true)
    UserEntity fromDto(UserDto dto);

}
