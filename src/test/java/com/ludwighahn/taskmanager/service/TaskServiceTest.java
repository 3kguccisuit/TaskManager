package com.ludwighahn.taskmanager.service;

import com.ludwighahn.taskmanager.dto.TaskDTO;
import com.ludwighahn.taskmanager.model.Task;
import com.ludwighahn.taskmanager.model.User;
import com.ludwighahn.taskmanager.repository.TaskRepository;
import com.ludwighahn.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTask() {
        // Arrange: Create TaskDTO and mock User and Task entities
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");

        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setTitle("Test Task");

        // Mock the repository responses
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act: Call the service method with TaskDTO and user ID
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO, 1L);

        // Assert: Verify that the task was created and converted to TaskDTO
        assertNotNull(createdTaskDTO);
        assertNotNull(createdTaskDTO.getTitle(), "Test Task");
    }
}
