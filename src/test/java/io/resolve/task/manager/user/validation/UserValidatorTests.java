package io.resolve.task.manager.user.validation;

import io.resolve.task.manager.exception.TaskManagerException;
import io.resolve.task.manager.user.model.UserEntity;
import io.resolve.task.manager.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTests {

    private static final Long ID = 1L;
    private static final String NAME = "Petko";
    private static final String EMAIL = "petkovpd@gmail.com";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidatorImpl userValidator;

    @Test
    void validateAndGetUserById_whenUserExists() {
        UserEntity user = new UserEntity(ID, NAME, EMAIL, null);

        Mockito.when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> this.userValidator.validateAndGetUserById(user.getId()));
    }

    @Test
    void validateAndGetUserById_whenUserNotExists() {
        Long userId = 2L;

        Mockito.when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.userValidator.validateAndGetUserById(userId));

        Assertions.assertEquals("There is no user with id: " + userId, ex.getMessage());
    }

    @Test
    void validateUserNameAndEmail_whenNameAndEmailExists() {
        UserEntity user = new UserEntity(ID, NAME, EMAIL, null);

        Assertions.assertDoesNotThrow(() -> this.userValidator.validateUserNameAndEmail(user));
    }

    @Test
    void validateUserNameAndEmail_whenNameNotExists() {
        UserEntity user = new UserEntity(ID, null, EMAIL, null);

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.userValidator.validateUserNameAndEmail(user));

        Assertions.assertEquals("User name is missing", ex.getMessage());
    }

    @Test
    void validateUserNameAndEmail_whenEmailNotExists() {
        UserEntity user = new UserEntity(ID, NAME, null, null);

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.userValidator.validateUserNameAndEmail(user));

        Assertions.assertEquals("User email is missing", ex.getMessage());
    }

    @Test
    void validateAndGetUserForUpdate_whenUserExists() {
        UserEntity user = new UserEntity(ID, NAME, EMAIL, null);

        Mockito.when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> this.userValidator.validateAndGetUserForUpdate(user));
    }

    @Test
    void validateAndGetUserForUpdate_whenUserIdNotExists() {
        UserEntity user = new UserEntity(null, NAME, EMAIL, null);

        TaskManagerException ex = Assertions.assertThrows(TaskManagerException.class, () ->
                this.userValidator.validateAndGetUserForUpdate(user));

        Assertions.assertEquals("This is not a valid user", ex.getMessage());
    }

}
