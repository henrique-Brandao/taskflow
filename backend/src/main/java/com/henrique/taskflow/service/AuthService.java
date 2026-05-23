package com.henrique.taskflow.service;

import com.henrique.taskflow.dto.request.LoginRequest;
import com.henrique.taskflow.dto.request.RegisterRequest;
import com.henrique.taskflow.dto.response.LoginResponse;
import com.henrique.taskflow.dto.response.UserResponse;
import com.henrique.taskflow.exceptions.InvalidCredentialsException;
import com.henrique.taskflow.exceptions.UserAlreadyExistsException;
import com.henrique.taskflow.mapper.UserMapper;
import com.henrique.taskflow.model.AppUser;
import com.henrique.taskflow.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, TokenService tokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }

    public UserResponse registerUser (RegisterRequest registerRequest) {
        Optional<AppUser> possibleUser = userRepository.findByEmail(registerRequest.email());

        if(possibleUser.isPresent()) {
            throw new UserAlreadyExistsException(registerRequest.email());
        }
        AppUser newUser = userMapper.toEntity(registerRequest, bCryptPasswordEncoder.encode(registerRequest.password()));
        AppUser savedUser = userRepository.save(newUser);

        return userMapper.toResponse(savedUser);
    }

    public LoginResponse loginUser (LoginRequest loginRequest) {
        Optional<AppUser> possibleUser = userRepository.findByEmail(loginRequest.email());
        if (possibleUser.isEmpty() || !bCryptPasswordEncoder.matches(loginRequest.password(), possibleUser.get().getPassword())) {
            throw new InvalidCredentialsException();
        }
        var user = possibleUser.get();
        return tokenService.generateToken(user);
    }
}
