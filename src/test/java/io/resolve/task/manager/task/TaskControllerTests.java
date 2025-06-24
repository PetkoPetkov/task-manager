package io.resolve.task.manager.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.resolve.task.manager.exception.TaskManagerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class TaskControllerTests {

    private static final String PATH = "/tasks";
    private static final Long ID = 1L;
    private static final String TITLE = "First task";
    private static final String DESCRIPTION = "Initial task";
    private static final String DUE_DATE = "2025-06-24";
    private static final String IN_PROGRESS = "IN_PROGRESS";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void createTask_withValidData_returnsCreatedTask() throws Exception {
        TaskDto taskDto = new TaskDto(null, TITLE, DESCRIPTION, DUE_DATE, IN_PROGRESS, null, null);

        mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.status").value(IN_PROGRESS));
    }

    @Test
    void findAllTasks_returnsAllTasks() throws Exception {

        mockMvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].title").value(TITLE))
                .andExpect(jsonPath("$[0].dueDate").value(DUE_DATE));
    }

    @Test
    void findById_returnsTask() throws Exception {
        mockMvc.perform(get(PATH + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.dueDate").value(DUE_DATE));
    }

    @Test
    void updateTask_whenAssignUser_shouldChangeTaskUser() throws Exception {
        String updatedTitle = "Task is updated";
        TaskDto taskDto = new TaskDto(ID, updatedTitle, DESCRIPTION, DUE_DATE, null, ID, null);

        mockMvc.perform(put(PATH + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task successfully updated"));
    }

    @Test
    void updateTask_whenStatusChange_shouldUpdate() throws Exception {
        TaskDto taskDto = new TaskDto(ID, TITLE, DESCRIPTION, DUE_DATE, "DONE", null, null);

        mockMvc.perform(put(PATH + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task successfully updated"));
    }

    @Test
    void updateTask_whenMissingStatusAndUser_shouldThrowValidationException() throws Exception {
        TaskDto taskDto = new TaskDto(ID, TITLE, DESCRIPTION, DUE_DATE, null, null, null);

        mockMvc.perform(put(PATH + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof TaskManagerException))
                .andExpect(result -> Assertions.assertEquals(
                        "You must provide status or user id", result.getResolvedException().getMessage()));
    }


    @Test
    void findAllByUserId_returnsAllTasksForUser() throws Exception {
        Long userId = 1L;
        TaskDto taskDto = new TaskDto(ID, TITLE, DESCRIPTION, DUE_DATE, null, userId, null);

        mockMvc.perform(put(PATH + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task successfully updated"));

        mockMvc.perform(get(PATH + "/user/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].userId").value(userId));
    }


}
