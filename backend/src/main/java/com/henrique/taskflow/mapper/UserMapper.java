package com.henrique.taskflow.mapper;

import com.henrique.taskflow.dto.request.RegisterRequest;
import com.henrique.taskflow.dto.response.UserResponse;
import com.henrique.taskflow.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUser toEntity(RegisterRequest request, String encryptedPassword) {
        var user = new AppUser();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(encryptedPassword);

        return user;
    }

    public UserResponse toResponse(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}