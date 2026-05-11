package Mapper;

import DTOs.Request.TaskRequest;
import DTOs.Response.TaskResponse;
import Model.TaskModel;

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

    public static void updateEntity(TaskModel entity, TaskRequest request) {
        if(request.title() != null && request.title().isBlank()) entity.setTitle(request.title());
        if(request.description() != null) entity.setDescription(request.description());
    }
}
