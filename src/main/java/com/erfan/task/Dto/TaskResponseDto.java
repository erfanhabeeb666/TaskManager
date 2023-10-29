package com.erfan.task.Dto;

import lombok.Data;

@Data

public class TaskResponseDto {
  private Long id;
    private String title;
    private String description;
    private int priority; 
    private String status;
    private String username;
}
