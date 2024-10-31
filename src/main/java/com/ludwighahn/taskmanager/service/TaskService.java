package com.ludwighahn.taskmanager.service;

import com.ludwighahn.taskmanager.dto.TaskDTO;
import com.ludwighahn.taskmanager.model.Task;
import com.ludwighahn.taskmanager.model.User;
import com.ludwighahn.taskmanager.repository.TaskRepository;
import com.ludwighahn.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Fetch all tasks for the authenticated user
    public List<TaskDTO> getAllTasks(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Create a new task for the authenticated user
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = convertToEntity(taskDTO);
        task.setUser(user);  // Set the authenticated user
        task = taskRepository.save(task);
        return convertToDTO(task);
    }

    // Update an existing task
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        task = taskRepository.save(task);
        return convertToDTO(task);
    }

    // Delete a task
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    // Helper methods to convert between DTO and entity
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        return dto;
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        return task;
    }
}
