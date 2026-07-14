package com.omnitrade.user_service.controller;

import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;
import com.omnitrade.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControler {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser (@RequestBody CreateUserRequest request){
        UserResponseDTO response = userService.createUser(request);
        return new ResponseEntity<>(response,  HttpStatus.CREATED);
    }
}
