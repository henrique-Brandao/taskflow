package com.henrique.taskflow.exceptions;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("task not found with id: " + id);
    }
}
