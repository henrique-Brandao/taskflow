package com.henrique.taskflow.Exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task não encontrada com o id: " + id);
    }
}
