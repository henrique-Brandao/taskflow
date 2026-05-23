package com.henrique.taskflow.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O e-mail é obrigatorio")
        @Email(message = "O e-mail deve ser valido")
        String email,
        @NotBlank(message = "Senha é obrigatoria")
        String password
) {
}
