package com.henrique.taskflow.mapper;

import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.request.TaskUpdateRequest;
import com.henrique.taskflow.dto.response.TaskResponse;
import com.henrique.taskflow.model.TaskModel;

public class TaskMapper {
    public static TaskModel toEntity(TaskRequest request) {
        TaskModel taskModel = new TaskModel();
        taskModel.setTitle(request.title());
        taskModel.setDescription(request.description());
        taskModel.setCompleted(false);
        return taskModel;
    }

    public static TaskResponse toResponse(TaskModel entity) {
        TaskResponse taskResponse = new TaskResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.isCompleted(),
                entity.getCreatedAt()
        );

        return taskResponse;
    }

    public static void updateEntity(TaskModel entity, TaskUpdateRequest request) {
        if(request.title() != null && request.title().isBlank()) entity.setTitle(request.title());
        if(request.description() != null) entity.setDescription(request.description());
        if(request.completed() != null) entity.setCompleted(request.completed());
    }
}
