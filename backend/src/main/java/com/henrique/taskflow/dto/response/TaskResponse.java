package com.henrique.taskflow.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt
) {
}
