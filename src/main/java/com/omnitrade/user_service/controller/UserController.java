package com.omnitrade.user_service.controller;

import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;
import com.omnitrade.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser (@Valid @RequestBody CreateUserRequest request){
        UserResponseDTO response = userService.createUser(request);
        return new ResponseEntity<>(response,  HttpStatus.CREATED);
    }
}
