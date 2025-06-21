package io.resolve.task.manager.user.service;

import io.resolve.task.manager.user.model.UserEntity;
import io.resolve.task.manager.user.repository.UserRepository;

import io.resolve.task.manager.user.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        this.userValidator.validateUserNameAndEmail(user);
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(UserEntity user) {
        UserEntity validUser = this.userValidator.validateAndGetUserForUpdate(user);
        validUser.setName(user.getName());
        validUser.setEmail(user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return this.userRepository.findAll();
    }
}
