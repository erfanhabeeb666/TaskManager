package com.erfan.task.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erfan.task.Models.User;

public interface UserRepository extends JpaRepository<User,Long> {



    Optional<User> findByUsername(String username);
    
}
