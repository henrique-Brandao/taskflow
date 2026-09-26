package com.henrique.taskflow.repository;

import com.henrique.taskflow.model.Task;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskRepository {

    private final DynamoDbTable<Task> taskTable;

    public TaskRepository(DynamoDbEnhancedClient enhancedClient) {
        this.taskTable = enhancedClient.table("Tasks", TableSchema.fromBean(Task.class));
    }

    public Optional<Task> findByIdAndUserId(String id, String userId) {
        Task task = taskTable.getItem(Key.builder().partitionValue(userId).sortValue(id).build());
        return Optional.ofNullable(task);
    }

    public List<Task> findAllByUserId(String userId) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(userId).build());
        return taskTable.query(queryConditional).stream()
                .flatMap(page -> page.items().stream())
                .collect(Collectors.toList());
    }

    public void save(Task task) {
        taskTable.putItem(task);
    }

    public void delete(Task task) {
        taskTable.deleteItem(task);
    }
}
