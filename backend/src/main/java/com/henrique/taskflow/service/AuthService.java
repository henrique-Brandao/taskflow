package com.henrique.taskflow.service;

import com.henrique.taskflow.dto.request.LoginRequest;
import com.henrique.taskflow.dto.request.RegisterRequest;
import com.henrique.taskflow.dto.response.LoginResponse;
import com.henrique.taskflow.dto.response.UserResponse;
import com.henrique.taskflow.exceptions.InvalidCredentialsException;
import com.henrique.taskflow.exceptions.UserAlreadyExistsException;
import com.henrique.taskflow.model.AppUser;
import com.henrique.taskflow.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public UserResponse registerUser (RegisterRequest registerRequest) {
        Optional<AppUser> possibleUser = userRepository.findByEmail(registerRequest.email());

        if(possibleUser.isPresent()) {
            throw new UserAlreadyExistsException(registerRequest.email());
        }
        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        AppUser newUser = new AppUser(
                UUID.randomUUID().toString(),
                registerRequest.name(),
                registerRequest.email(),
                hashedPassword,
                LocalDateTime.now().toString()
        );
        userRepository.save(newUser);

        return new UserResponse(UUID.fromString(newUser.getId()), newUser.getName(), newUser.getEmail()); 
    }

    public LoginResponse loginUser (LoginRequest loginRequest) {
        Optional<AppUser> possibleUser = userRepository.findByEmail(loginRequest.email());
        if (possibleUser.isEmpty() || !BCrypt.checkpw(loginRequest.password(), possibleUser.get().getPassword())) {
            throw new InvalidCredentialsException();
        }
        var user = possibleUser.get();
        return tokenService.generateToken(user);
    }
}
