package io.resolve.task.manager.user.service;

import io.resolve.task.manager.user.model.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity create(UserEntity user);

    void update(UserEntity user);

    List<UserEntity> findAll();

}
