package com.henrique.taskflow.mapper;

import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.request.TaskUpdateRequest;
import com.henrique.taskflow.dto.response.TaskResponse;
import com.henrique.taskflow.model.Task;

public class TaskMapper {
    public static Task toEntity(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(false);
        return task;
    }

    public static TaskResponse toResponse(Task entity) {
        TaskResponse taskResponse = new TaskResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.isCompleted(),
                entity.getCreatedAt()
        );

        return taskResponse;
    }

    public static void updateEntity(Task entity, TaskUpdateRequest request) {
        if(request.title() != null && request.title().isBlank()) entity.setTitle(request.title());
        if(request.description() != null) entity.setDescription(request.description());
        if(request.completed() != null) entity.setCompleted(request.completed());
    }
}
