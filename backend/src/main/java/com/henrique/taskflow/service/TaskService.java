package com.henrique.taskflow.service;


import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.request.TaskUpdateRequest;
import com.henrique.taskflow.dto.response.TaskResponse;
import com.henrique.taskflow.exceptions.TaskNotFoundException;
import com.henrique.taskflow.exceptions.UserNotFoundException;
import com.henrique.taskflow.mapper.TaskMapper;
import com.henrique.taskflow.mapper.UserMapper;
import com.henrique.taskflow.model.AppUser;
import com.henrique.taskflow.model.Task;
import com.henrique.taskflow.repository.TaskRepository;
import com.henrique.taskflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public TaskService(TaskRepository taskRepository, UserRepository userRepository, UserMapper userMapper) {
        this.repository = taskRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public TaskResponse createTask(TaskRequest taskRequest, UUID userId) {
        Task newTask = TaskMapper.toEntity(taskRequest);
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        newTask.setUser(user);
        return TaskMapper.toResponse(repository.save(newTask));
    }

    public List<TaskResponse> listTasks(UUID userId) {
        List<Task> taskList = repository.findAllByUserId(userId);
        return taskList.stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    public TaskResponse findTaskById(UUID taskId, UUID userId) {
       Task task = repository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new TaskNotFoundException(taskId));
        return TaskMapper.toResponse(task);
    }

    public TaskResponse updateTask(TaskUpdateRequest request, UUID taskId, UUID userId) {
        Task task = repository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new TaskNotFoundException(taskId));
        TaskMapper.updateEntity(task, request);
        Task updatedTask = repository.save(task);
        return TaskMapper.toResponse(updatedTask);
    }

    public void deleteTask(UUID taskId, UUID userId) {
        Task task = repository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new TaskNotFoundException(taskId));
        repository.deleteById(task.getId());
    }
}
