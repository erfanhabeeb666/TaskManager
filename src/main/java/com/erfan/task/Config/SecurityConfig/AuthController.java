package com.erfan.task.Config.SecurityConfig;

import com.erfan.task.Models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody User user) {
    User registeredUser = authService.register(user);
    return ResponseEntity.ok("user registered");
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestBody User user) {
    return ResponseEntity.ok(authService.authenticate(user));
  }
}
