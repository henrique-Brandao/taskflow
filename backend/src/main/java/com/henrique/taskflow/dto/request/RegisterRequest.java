package com.henrique.taskflow.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "O nome de usuario é obrigatorio")
        @Size(min = 1, max = 255, message = " O nome muito longo")
        String name,

        @Email(message = "O e-mail deve ser valido")
        @NotBlank(message = "O e-mail é obrigatorio")
        String email,

        @NotBlank(message = "Senha é obrigatoria")
        String password
) {}
