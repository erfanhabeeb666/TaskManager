package com.erfan.task.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erfan.task.Models.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {

    boolean existsByTitle(String title);

    Optional<Task> findByTitle(String tasktitle);

    List<Task> findByUserId(Long userId);
    
}
