package com.henrique.taskflow.dto.response;

public record ErrorResponse(
        String message,
        int status,
        String error
) {
}
