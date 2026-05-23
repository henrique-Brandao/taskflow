package com.henrique.taskflow.dto.response;

public record LoginResponse (
        String acessToken,
        Long expiresIn
) {}
