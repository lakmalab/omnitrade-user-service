package com.omnitrade.user_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String username ;
    private String firstName ;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String district;
    private String profileImage;
}
