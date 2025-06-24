package io.resolve.task.manager.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.resolve.task.manager.exception.TaskManagerException;
import io.resolve.task.manager.user.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class UserControllerTests {

    private static final Long ID = 1L;
    private static final String NAME = "Petko";
    private static final String EMAIL = "petkovpd@gmail.com";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void createUser_withValidData_returnsCreatedUser() throws Exception {
        UserDto inputDto = new UserDto();
        inputDto.setName(NAME);
        inputDto.setEmail(EMAIL);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    void updateUser_withValidData_shouldUpdateUser() throws Exception {
        UserDto inputDto = new UserDto();
        inputDto.setId(ID);
        inputDto.setName(NAME);
        inputDto.setEmail(EMAIL);

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully updated"));
    }

    @Test
    void updateUser_whenUserNotExists_shouldThrowValidationException() throws Exception {
        UserDto inputDto = new UserDto();
        Long missingUserId = 100L;
        inputDto.setId(missingUserId);
        inputDto.setName(NAME);
        inputDto.setEmail(EMAIL);

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof TaskManagerException))
                .andExpect(result -> Assertions.assertEquals(
                        "There is no user with id: " + missingUserId, result.getResolvedException().getMessage()));
    }

    @Test
    void findAllUsers_shouldReturnAll() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[1].name").value("Petko Petkov"))
                .andExpect(jsonPath("$[1].email").value("petkov@gmail.com"));
    }
}
