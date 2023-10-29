package com.erfan.task.Exceptions;


public class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
        super(message);
     }
}
