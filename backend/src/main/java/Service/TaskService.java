package Service;

import DTOs.Request.TaskRequest;
import DTOs.Response.TaskResponse;
import Exceptions.TaskNotFoundException;
import Mapper.TaskMapper;
import Model.TaskModel;
import Repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

private final TaskRepository repository;

    public TaskService(TaskRepository taskRepository) {
        this.repository = taskRepository;
    }

    // CRUD

    // CREATE
    public TaskResponse createTask(TaskRequest taskRequest) {
        TaskModel newTask = TaskMapper.toEntity(taskRequest);
        return TaskMapper.toResponse(repository.save(newTask));
    }

    //READ
    public List<TaskResponse> ListTasks() {
        List<TaskResponse> taskList = repository.findAll()
                .stream()
                .map(TaskMapper::toResponse)
                .toList();

        return taskList;
    }

    //READ
    public TaskResponse findTaskById(Long id) {
       TaskModel task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.toResponse(task);
    }

    // UPDATE

    public TaskResponse updateTask(TaskRequest request, Long id) {
        TaskModel taskModel = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        TaskMapper.updateEntity(taskModel, request);
        TaskModel updatedTask = repository.save(taskModel);
        return TaskMapper.toResponse(updatedTask);
    }

    // DELETE

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
