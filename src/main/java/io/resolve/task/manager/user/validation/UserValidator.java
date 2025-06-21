package io.resolve.task.manager.user.validation;

import io.resolve.task.manager.user.model.UserEntity;

public interface UserValidator {

    UserEntity validateAndGetUserById(Long userId);

    void validateUserNameAndEmail(UserEntity user);

    UserEntity validateAndGetUserForUpdate(UserEntity user);
}
