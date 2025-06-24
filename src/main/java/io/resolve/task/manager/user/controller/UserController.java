package io.resolve.task.manager.user.controller;

import io.resolve.task.manager.user.mapper.UserMapper;
import io.resolve.task.manager.user.model.UserEntity;
import io.resolve.task.manager.user.service.UserService;
import io.resolve.task.manager.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public UserController(final UserMapper userMapper,
                          final UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {

        UserEntity userEntity = this.userService.create(this.userMapper.fromDto(dto));

        return ResponseEntity.status(HttpStatus.OK).body(this.userMapper.toDto(userEntity));
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<String> updateUser(@RequestBody UserDto dto) {

        this.userService.update(this.userMapper.fromDto(dto));

        return ResponseEntity.status(HttpStatus.OK).body("User successfully updated");
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.findAll()
                        .stream()
                        .map(this.userMapper::toDto)
                        .collect(Collectors.toList()));
    }

}
