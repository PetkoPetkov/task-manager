package io.resolve.task.manager.user.validation;

import io.resolve.task.manager.exception.TaskManagerException;
import io.resolve.task.manager.user.model.UserEntity;
import io.resolve.task.manager.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorImpl implements UserValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidatorImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity validateAndGetUserById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> new TaskManagerException("There is no user with id: " + userId));
    }

    @Override
    public void validateUserNameAndEmail(UserEntity user) {
        if (user.getName() == null) {
            throw new TaskManagerException("User name is missing");
        }
        if (user.getEmail() == null) {
            throw new TaskManagerException("User email is missing");
        }
    }

    @Override
    public UserEntity validateAndGetUserForUpdate(UserEntity user) {
        validateUserId(user);
        validateUserNameAndEmail(user);
        return validateAndGetUserById(user.getId());
    }

    private void validateUserId(UserEntity user) {
        if (user == null || user.getId() == null) {
            throw new TaskManagerException("This is not a valid user");
        }
    }
}
