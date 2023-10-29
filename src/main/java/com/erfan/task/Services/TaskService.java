package com.erfan.task.Services;

import com.erfan.task.Config.SecurityConfig.JwtService;
import com.erfan.task.Dto.TaskDTO;
import com.erfan.task.Dto.TaskResponseDto;
import com.erfan.task.Exceptions.TaskAlreadyExistsException;
import com.erfan.task.Exceptions.TaskNotFoundException;
import com.erfan.task.Models.Task;
import com.erfan.task.Repositories.TaskRepository;
import com.erfan.task.Utils.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final JwtService jwtService;
  private final UserService userService;

  public TaskService(
    TaskRepository taskRepository,
    JwtService jwtService,
    UserService userService
  ) {
    this.taskRepository = taskRepository;
    this.jwtService = jwtService;
    this.userService = userService;
  }

  public Response<TaskResponseDto> createTaskResponse(
    String token,
    TaskDTO taskDTO
  ) {
    Long userId = Long.valueOf(jwtService.extractId(token));

    Task task = new Task();
    task.setTitle(taskDTO.getTitle());
    task.setDescription(taskDTO.getDescription());
    task.setPriority(taskDTO.getPriority());
    task.setStatus(taskDTO.getStatus());
    task.setUser(userService.getUserUsingId(userId));

    if (taskRepository.existsByTitle(task.getTitle())) {
      throw new TaskAlreadyExistsException(
        "Task with the same title already exists."
      );
    }

    Task createdTask = taskRepository.save(task);
    TaskResponseDto responseDto = mapTaskToTaskResponseDto(createdTask);

    return buildTaskResponse(
      HttpStatus.CREATED,
      "Task created successfully",
      responseDto
    );
  }

  public ResponseEntity<Response<TaskResponseDto>> updateTaskForCurrentUser(
    String token,
    Long taskId,
    Task updatedTask
  ) {
    Long userId = Long.valueOf(jwtService.extractId(token));

    Task existingTask = taskRepository
      .findById(taskId)
      .orElseThrow(() ->
        new TaskNotFoundException("Task with id " + taskId + " not found")
      );

    if (!existingTask.getUser().getId().equals(userId)) {
      throw new TaskNotFoundException(
        "Task with id " + taskId + " is not made by the current user"
      );
    }

    existingTask.setTitle(updatedTask.getTitle());
    existingTask.setDescription(updatedTask.getDescription());
    existingTask.setPriority(updatedTask.getPriority());
    existingTask.setStatus(updatedTask.getStatus());

    Task updated = taskRepository.save(existingTask);
    TaskResponseDto responseDto = mapTaskToTaskResponseDto(updated); // Define this mapping method

    Response<TaskResponseDto> response = Response
      .<TaskResponseDto>builder()
      .timeStamp(LocalDateTime.now())
      .statusCode(HttpStatus.OK.value())
      .status(HttpStatus.OK)
      .message("Task updated successfully")
      .data(responseDto)
      .build();

    return ResponseEntity.ok(response);
  }

  public ResponseEntity<Response<Void>> deleteTaskForCurrentUser(
    String token,
    Long taskId
  ) {
    Long userId = Long.valueOf(jwtService.extractId(token));

    Task existingTask = taskRepository
      .findById(taskId)
      .orElseThrow(() ->
        new TaskNotFoundException("Task with id " + taskId + " not found")
      );

    if (!existingTask.getUser().getId().equals(userId)) {
      throw new TaskNotFoundException(
        "Task with id " + taskId + " not found for the current user."
      );
    }

    taskRepository.delete(existingTask);

    Response<Void> response = Response
      .<Void>builder()
      .timeStamp(LocalDateTime.now())
      .statusCode(HttpStatus.NO_CONTENT.value())
      .status(HttpStatus.NO_CONTENT)
      .message("Task deleted successfully")
      .build();

    return ResponseEntity.ok(response);
  }

  public ResponseEntity<Response<TaskResponseDto>> getTaskByTitleForCurrentUser(
    String token,
    String tasktitle
  ) {
    Long userId = Long.valueOf(jwtService.extractId(token));
    Task task = taskRepository
      .findByTitle(tasktitle)
      .orElseThrow(() ->
        new TaskNotFoundException("Task with title " + tasktitle + " not found")
      );

    if (!task.getUser().getId().equals(userId)) {
      throw new TaskNotFoundException(
        "Task with title " + tasktitle + " not found for the current user."
      );
    }

    TaskResponseDto responseDto = mapTaskToTaskResponseDto(task); // Define this mapping method

    Response<TaskResponseDto> response = Response
      .<TaskResponseDto>builder()
      .timeStamp(LocalDateTime.now())
      .statusCode(HttpStatus.OK.value())
      .status(HttpStatus.OK)
      .message("Task retrieved successfully")
      .data(responseDto)
      .build();

    return ResponseEntity.ok(response);
  }

  public ResponseEntity<Response<List<TaskResponseDto>>> getTasksByUserIdFromJwt(
    String token
  ) {
    Long userId = Long.valueOf(jwtService.extractId(token));
    List<Task> tasks = taskRepository.findByUserId(userId);

    List<TaskResponseDto> responseDtos = tasks
      .stream()
      .map(this::mapTaskToTaskResponseDto) // Define this mapping method
      .collect(Collectors.toList());

    Response<List<TaskResponseDto>> response = Response
      .<List<TaskResponseDto>>builder()
      .timeStamp(LocalDateTime.now())
      .statusCode(HttpStatus.OK.value())
      .status(HttpStatus.OK)
      .message("Tasks retrieved successfully")
      .data(responseDtos)
      .build();

    return ResponseEntity.ok(response);
  }

  public TaskResponseDto convertToDto(Task task) {
    TaskResponseDto taskResponseDto = new TaskResponseDto();
    BeanUtils.copyProperties(task, taskResponseDto);
    return taskResponseDto;
  }

  public List<TaskResponseDto> convertToDtoList(List<Task> tasks) {
    return tasks.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  private Response<TaskResponseDto> buildTaskResponse(
    HttpStatus status,
    String message,
    TaskResponseDto data
  ) {
    return Response
      .<TaskResponseDto>builder()
      .timeStamp(LocalDateTime.now())
      .statusCode(status.value())
      .status(status)
      .message(message)
      .data(data)
      .build();
  }

  private TaskResponseDto mapTaskToTaskResponseDto(Task task) {
    TaskResponseDto responseDto = new TaskResponseDto();
    responseDto.setId(task.getId());
    responseDto.setTitle(task.getTitle());
    responseDto.setDescription(task.getDescription());
    responseDto.setPriority(task.getPriority());
    responseDto.setStatus(task.getStatus());
    responseDto.setUsername(task.getUser().getUsername());
    return responseDto;
  }
}
