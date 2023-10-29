package com.erfan.task.Services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.erfan.task.Models.User;
import com.erfan.task.Repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public String getIdfromUsername(String username){
        return String.valueOf(userRepository.findByUsername(username).get().getId());
    }

    public User getUserUsingId(Long userId) {
        return userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    
}
