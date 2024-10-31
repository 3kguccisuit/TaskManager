package com.ludwighahn.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludwighahn.taskmanager.dto.TaskDTO;
import com.ludwighahn.taskmanager.service.AuthenticationService;
import com.ludwighahn.taskmanager.service.TaskService;
import com.ludwighahn.taskmanager.util.JwtUtil;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void createTask() throws Exception {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("Finish the task management system 2");
        taskDTO.setStatus("pending");
        taskDTO.setPriority("high");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        taskDTO.setDueDate(dateFormat.parse("2024-10-30"));

        TaskDTO createdTask = new TaskDTO();
        createdTask.setId(1L);
        createdTask.setTitle("New Task");

        // Generate a valid JWT token using JwtUtil
        String validJwtToken = "Bearer " + jwtUtil.generateToken("testUser");

        when(authenticationService.getAuthenticatedUserId(Mockito.any())).thenReturn(1L);
        when(taskService.createTask(Mockito.any(TaskDTO.class), Mockito.eq(1L))).thenReturn(createdTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks/create")
                .header("Authorization", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateTask() throws Exception {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Updated Task");

        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");

        String validJwtToken = "Bearer " + jwtUtil.generateToken("testUser");

        when(taskService.updateTask(Mockito.eq(1L), Mockito.any(TaskDTO.class))).thenReturn(updatedTask);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                .header("Authorization", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    void deleteTask() throws Exception {
        // No arrangement needed for delete
        String validJwtToken = "Bearer " + jwtUtil.generateToken("testUser");
        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1")
                .header("Authorization", validJwtToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllTasks() throws Exception {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Task 1");
        List<TaskDTO> tasks = Collections.singletonList(taskDTO);
        String validJwtToken = "Bearer " + jwtUtil.generateToken("testUser");
        when(authenticationService.getAuthenticatedUserId(Mockito.any())).thenReturn(1L);
        when(taskService.getAllTasks(1L)).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/all")
                .header("Authorization", validJwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }
}
