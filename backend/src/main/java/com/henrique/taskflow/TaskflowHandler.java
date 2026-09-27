package com.henrique.taskflow;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.henrique.taskflow.dto.request.LoginRequest;
import com.henrique.taskflow.dto.request.RegisterRequest;
import com.henrique.taskflow.dto.request.TaskRequest;
import com.henrique.taskflow.dto.request.TaskUpdateRequest;
import com.henrique.taskflow.repository.TaskRepository;
import com.henrique.taskflow.repository.UserRepository;
import com.henrique.taskflow.service.AuthService;
import com.henrique.taskflow.service.TaskService;
import com.henrique.taskflow.service.TokenService;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.HashMap;
import java.util.Map;

public class TaskflowHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final TaskService taskService;
    private final TokenService tokenService;

    public TaskflowHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new ParameterNamesModule());

        DynamoDbClient dynamoDbClient = DynamoDbClient.create();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        UserRepository userRepository = new UserRepository(enhancedClient);
        TaskRepository taskRepository = new TaskRepository(enhancedClient);
        this.tokenService = new TokenService();

        this.authService = new AuthService(userRepository, tokenService);
        this.taskService = new TaskService(taskRepository, userRepository);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String path = request.getPath();
        String method = request.getHttpMethod();

        try {
            if ("OPTIONS".equals(method)) {
                return createResponse(200, null);
            }

            if ("/auth/register".equals(path) && "POST".equals(method)) {
                RegisterRequest req = objectMapper.readValue(request.getBody(), RegisterRequest.class);
                return createResponse(201, authService.registerUser(req));
            } else if ("/auth/login".equals(path) && "POST".equals(method)) {
                LoginRequest req = objectMapper.readValue(request.getBody(), LoginRequest.class);
                return createResponse(200, authService.loginUser(req));
            } else if (path.startsWith("/task")) {
                String authHeader = request.getHeaders().get("Authorization");
                if (authHeader == null) {
                    authHeader = request.getHeaders().getOrDefault("authorization", "");
                }
                
                if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
                    return createResponse(401, Map.of("error", "Unauthorized"));
                }
                
                String token = authHeader.replace("Bearer ", "").trim();
                String userId = tokenService.validateTokenAndGetUserId(token);
                
                if (userId == null) {
                    return createResponse(401, Map.of("error", "Invalid or expired token"));
                }

                if ("POST".equals(method) && "/task".equals(path)) {
                    TaskRequest req = objectMapper.readValue(request.getBody(), TaskRequest.class);
                    return createResponse(201, taskService.createTask(req, userId));
                } else if ("GET".equals(method) && "/task".equals(path)) {
                    return createResponse(200, taskService.listTasks(userId));
                } else if ("GET".equals(method) && path.matches("/task/.*")) {
                    String taskId = path.substring(6);
                    return createResponse(200, taskService.findTaskById(taskId, userId));
                } else if (("PUT".equals(method) || "PATCH".equals(method)) && path.matches("/task/.*")) {
                    String taskId = path.substring(6);
                    TaskUpdateRequest req = objectMapper.readValue(request.getBody(), TaskUpdateRequest.class);
                    return createResponse(200, taskService.updateTask(req, taskId, userId));
                } else if ("DELETE".equals(method) && path.matches("/task/.*")) {
                    String taskId = path.substring(6);
                    taskService.deleteTask(taskId, userId);
                    return createResponse(204, null);
                }
            }
            
            return createResponse(404, Map.of("error", "Not Found"));

        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            return createResponse(500, Map.of("error", e.getMessage()));
        }
    }

    private APIGatewayProxyResponseEvent createResponse(int statusCode, Object body) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(statusCode);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        try {
            if (body != null) {
                response.setBody(objectMapper.writeValueAsString(body));
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setBody("{\"error\": \"Error serializing response\"}");
        }
        return response;
    }
}
