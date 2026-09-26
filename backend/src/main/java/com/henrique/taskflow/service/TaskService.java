package com.henrique.taskflow.service;

import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.request.TaskUpdateRequest;
import com.henrique.taskflow.dto.response.TaskResponse;
import com.henrique.taskflow.exceptions.TaskNotFoundException;
import com.henrique.taskflow.exceptions.UserNotFoundException;
import com.henrique.taskflow.model.AppUser;
import com.henrique.taskflow.model.Task;
import com.henrique.taskflow.repository.TaskRepository;
import com.henrique.taskflow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.repository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse createTask(TaskRequest taskRequest, String userId) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(UUID.fromString(userId)));
        Task newTask = new Task(
                userId,
                UUID.randomUUID().toString(),
                taskRequest.title(),
                taskRequest.description(),
                false,
                LocalDateTime.now().toString()
        );
        repository.save(newTask);
        return new TaskResponse(UUID.fromString(newTask.getId()), newTask.getTitle(), newTask.getDescription(), newTask.isCompleted(), LocalDateTime.parse(newTask.getCreatedAt()));
    }

    public List<TaskResponse> listTasks(String userId) {
        List<Task> taskList = repository.findAllByUserId(userId);
        return taskList.stream()
                .map(t -> new TaskResponse(UUID.fromString(t.getId()), t.getTitle(), t.getDescription(), t.isCompleted(), LocalDateTime.parse(t.getCreatedAt())))
                .collect(Collectors.toList());
    }

    public TaskResponse findTaskById(String taskId, String userId) {
        Task task = repository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new TaskNotFoundException(UUID.fromString(taskId)));
        return new TaskResponse(UUID.fromString(task.getId()), task.getTitle(), task.getDescription(), task.isCompleted(), LocalDateTime.parse(task.getCreatedAt()));
    }

    public TaskResponse updateTask(TaskUpdateRequest request, String taskId, String userId) {
        Task task = repository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new TaskNotFoundException(UUID.fromString(taskId)));
        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.completed() != null) task.setCompleted(request.completed());
        repository.save(task);
        return new TaskResponse(UUID.fromString(task.getId()), task.getTitle(), task.getDescription(), task.isCompleted(), LocalDateTime.parse(task.getCreatedAt()));
    }

    public void deleteTask(String taskId, String userId) {
        Task task = repository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new TaskNotFoundException(UUID.fromString(taskId)));
        repository.delete(task);
    }
}
