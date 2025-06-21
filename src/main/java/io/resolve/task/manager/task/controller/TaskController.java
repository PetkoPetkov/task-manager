package io.resolve.task.manager.task.controller;

import io.resolve.task.manager.task.TaskDto;
import io.resolve.task.manager.task.mapper.TaskMapper;
import io.resolve.task.manager.task.model.TaskEntity;
import io.resolve.task.manager.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @Autowired
    public TaskController(final TaskMapper taskMapper, final TaskService taskService) {
        this.taskMapper = taskMapper;
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto dto) {

        TaskEntity taskEntity = this.taskService.create(this.taskMapper.fromDto(dto));

        return ResponseEntity.status(HttpStatus.OK).body(this.taskMapper.toDto(taskEntity));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TaskDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.taskService.findAll()
                        .stream()
                        .map(this.taskMapper::toDto)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TaskDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.taskMapper.toDto(this.taskService.findById(id)));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> updateStatusOrAssignee(@PathVariable Long id, @RequestBody TaskDto dto) {
        TaskEntity taskEntity = this.taskMapper.fromDto(dto);
        this.taskService.update(id, taskEntity.getStatus(), taskEntity.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body("Task successfully updated");
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<TaskDto>> findAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.taskService.findByUserId(userId)
                        .stream()
                        .map(this.taskMapper::toDto)
                        .collect(Collectors.toList()));
    }

}
