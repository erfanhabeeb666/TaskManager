package com.erfan.task.Dto;

import lombok.Data;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private int priority;
    private String status;
}

