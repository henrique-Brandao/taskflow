package com.henrique.taskflow.controller;

import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.request.TaskUpdateRequest;
import com.henrique.taskflow.dto.response.TaskResponse;
import com.henrique.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

   @PostMapping
   public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTask(request, UUID.fromString(jwt.getSubject())));
   }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listTasks(UUID.fromString(jwt.getSubject())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.findTaskById(id, UUID.fromString(jwt.getSubject())));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> editTask(
            @PathVariable UUID id,
            @RequestBody @Valid TaskUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.updateTask(request, id, UUID.fromString(jwt.getSubject())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        service.deleteTask(id, UUID.fromString(jwt.getSubject()));
        return ResponseEntity.noContent().build();
    }

}
