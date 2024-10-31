package com.ludwighahn.taskmanager.controller;

import com.ludwighahn.taskmanager.dto.TaskDTO;
import com.ludwighahn.taskmanager.service.AuthenticationService;
import com.ludwighahn.taskmanager.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks(HttpServletRequest request) {
        Long userId = authenticationService.getAuthenticatedUserId(request); // Use the existing method
        List<TaskDTO> tasks = taskService.getAllTasks(userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO, HttpServletRequest request) {
        Long userId = authenticationService.getAuthenticatedUserId(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TaskDTO newTask = taskService.createTask(taskDTO, userId);
        return ResponseEntity.ok(newTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
