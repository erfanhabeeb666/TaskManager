package com.erfan.task.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.erfan.task.Dto.TaskDTO;
import com.erfan.task.Dto.TaskResponseDto;
import com.erfan.task.Models.Task;
import com.erfan.task.Services.TaskService;
import com.erfan.task.Utils.JwtUtils;
import com.erfan.task.Utils.Response;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final HttpServletRequest request;
    private final JwtUtils jwtUtils;

    public TaskController(TaskService taskService, HttpServletRequest request, JwtUtils jwtUtils) {
        this.taskService = taskService;
        this.request = request;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/createtasks")
    public ResponseEntity<Response<TaskResponseDto>> createTask(@RequestBody TaskDTO taskDTO) {
        Response<TaskResponseDto> response = taskService.createTaskResponse(jwtUtils.getJwtFromRequest(request), taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/updatetasks/{taskId}")
    public ResponseEntity<Response<TaskResponseDto>> updateTaskForCurrentUser(@PathVariable Long taskId, @RequestBody Task task) {
      
        return taskService.updateTaskForCurrentUser(jwtUtils.getJwtFromRequest(request), taskId, task);
    }

    @DeleteMapping("/deletetasks/{taskId}")
    public ResponseEntity<Response<Void>> deleteTaskForCurrentUser(@PathVariable Long taskId) {
        
        return taskService.deleteTaskForCurrentUser(jwtUtils.getJwtFromRequest(request), taskId);
    }

    @GetMapping("/viewalltasks")
    public ResponseEntity<Response<List<TaskResponseDto>>> getTasksForCurrentUser() {
        return taskService.getTasksByUserIdFromJwt(jwtUtils.getJwtFromRequest(request));
    }

    @GetMapping("/tasks/{tasktitle}")
    public ResponseEntity<Response<TaskResponseDto>> getTaskByTitleForCurrentUser(@PathVariable String tasktitle) {
        return taskService.getTaskByTitleForCurrentUser(jwtUtils.getJwtFromRequest(request), tasktitle);
    }
}
