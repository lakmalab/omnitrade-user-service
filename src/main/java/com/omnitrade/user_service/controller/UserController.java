package com.omnitrade.user_service.controller;

import com.omnitrade.user_service.model.dto.CreateUserRequest;
import com.omnitrade.user_service.model.dto.UpdateUserRequest;
import com.omnitrade.user_service.model.dto.UserResponseDTO;
import com.omnitrade.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getProfile (@Valid @PathVariable UUID id){
        UserResponseDTO response = userService.getProfile(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateProfile (@Valid @RequestBody UpdateUserRequest request){
        UserResponseDTO response = userService.updateProfile(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDTO>> getAllProfiles (){
        List<UserResponseDTO> response = userService.getAllProfiles();
        return ResponseEntity.ok(response);
    }
}
