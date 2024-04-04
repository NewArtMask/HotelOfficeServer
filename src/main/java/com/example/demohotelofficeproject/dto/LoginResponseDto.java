package com.example.demohotelofficeproject.dto;

import com.example.demohotelofficeproject.enums.Role;
import com.example.demohotelofficeproject.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class LoginResponseDto {
    private String userId;
    private String name;
    private String surname;
    private String email;
    private Role role;
    private UserStatus userStatus;
    private String token;
}
