package com.henrique.taskflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
        @Size(max = 120, message = "O titulo deve ter no máximo 120 caracteres")
        String title,
        String description,
        Boolean completed
) {
}
