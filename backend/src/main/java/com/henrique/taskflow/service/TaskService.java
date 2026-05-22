package com.henrique.taskflow.service;


import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.request.TaskUpdateRequest;
import com.henrique.taskflow.dto.response.TaskResponse;
import com.henrique.taskflow.exceptions.TaskNotFoundException;
import com.henrique.taskflow.mapper.TaskMapper;
import com.henrique.taskflow.model.Task;
import com.henrique.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

private final TaskRepository repository;

    public TaskService(TaskRepository taskRepository) {
        this.repository = taskRepository;
    }

    // CRUD

    // CREATE
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task newTask = TaskMapper.toEntity(taskRequest);
        return TaskMapper.toResponse(repository.save(newTask));
    }

    //READ
    public List<TaskResponse> listTasks() {
    return repository.findAll()
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    //READ
    public TaskResponse findTaskById(UUID id) {
       Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.toResponse(task);
    }

    // UPDATE

    public TaskResponse updateTask(TaskUpdateRequest request, UUID id) {
        Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        TaskMapper.updateEntity(task, request);
        Task updatedTask = repository.save(task);
        return TaskMapper.toResponse(updatedTask);
    }

    // DELETE

    public void deleteTask(UUID id) {
        Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        repository.deleteById(id);
    }
}
