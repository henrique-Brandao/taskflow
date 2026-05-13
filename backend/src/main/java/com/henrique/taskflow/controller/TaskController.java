package com.henrique.taskflow.controller;

import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.response.TaskResponse;
import com.henrique.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

   @PostMapping
   public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTask(request));
   }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity.ok(service.listTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findTaskById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> editTask(
            @PathVariable Long id,
            @RequestBody @Valid TaskRequest request
    ) {
        return ResponseEntity.ok(service.updateTask(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
