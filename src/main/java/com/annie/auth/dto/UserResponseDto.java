package com.annie.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private String email;
    private String role;
    private String token;
}
