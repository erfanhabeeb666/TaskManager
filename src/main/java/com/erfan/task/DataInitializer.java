package com.erfan.task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.erfan.task.Models.User;
import com.erfan.task.Models.Task;
import com.erfan.task.Repositories.UserRepository;
import com.erfan.task.Repositories.TaskRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, TaskRepository taskRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
     
        User user1 = new User();
        user1.setUsername("erfan");
        user1.setPassword(passwordEncoder.encode("erfan"));
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("mahin");
        user2.setPassword(passwordEncoder.encode("mahin"));
        userRepository.save(user2);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description for Task 1");
        task1.setPriority(1);
        task1.setStatus("Todo");
        task1.setUser(user1);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description for Task 2");
        task2.setPriority(2);
        task2.setStatus("In Progress");
        task2.setUser(user1);
        taskRepository.save(task2);

        Task task3 = new Task();
        task3.setTitle("Task 3");
        task3.setDescription("Description for Task 3");
        task3.setPriority(3);
        task3.setStatus("Done");
        task3.setUser(user2);
        taskRepository.save(task3);
    }
}
