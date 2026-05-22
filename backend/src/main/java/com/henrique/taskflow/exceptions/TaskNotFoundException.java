package com.henrique.taskflow.exceptions;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task não encontrada com o id: " + id);
    }
}
