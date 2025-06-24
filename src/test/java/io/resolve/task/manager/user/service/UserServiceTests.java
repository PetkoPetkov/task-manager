package io.resolve.task.manager.user.service;

import io.resolve.task.manager.user.model.UserEntity;
import io.resolve.task.manager.user.repository.UserRepository;
import io.resolve.task.manager.user.validation.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    private static final Long ID = 1L;
    private static final String NAME = "Petko";
    private static final String EMAIL = "petkovpd@gmail.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_returnsCreatedUser() {
        UserEntity user = new UserEntity(ID, NAME, EMAIL, null);

        Mockito.when(this.userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);
        Mockito.doNothing().when(this.userValidator).validateUserNameAndEmail(user);

        UserEntity createdUser = this.userService.create(user);

        Assertions.assertNotNull(createdUser);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals(ID, createdUser.getId());
    }

    @Test
    void updateUser_shouldUpdateUser() {
        UserEntity updateUser = new UserEntity(ID, NAME, EMAIL, null);
        UserEntity existingUser = new UserEntity(ID, "Old Name", "old@example.com", null);

        Mockito.when(this.userValidator.validateAndGetUserForUpdate(updateUser)).thenReturn(existingUser);

        this.userService.update(updateUser);

        Assertions.assertEquals(updateUser.getName(), existingUser.getName());
        Assertions.assertEquals(updateUser.getEmail(), existingUser.getEmail());

        Mockito.verify(this.userValidator, Mockito.times(1)).validateAndGetUserForUpdate(updateUser);
    }

    @Test
    void findAllUsers_shouldReturnAll() {
        List<UserEntity> users = List.of(
                new UserEntity(ID, NAME, EMAIL, null),
                new UserEntity(2L, "Petkov", "petkov@gmail.com", null)
        );

        Mockito.when(this.userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = this.userService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Petko", result.get(0).getName());
        Assertions.assertEquals("Petkov", result.get(1).getName());

        Mockito.verify(this.userRepository, Mockito.times(1)).findAll();

    }

}
