package com.henrique.taskflow.dto.response;

public record LoginResponse (
        String accessToken,
        Long expiresIn
) {}
