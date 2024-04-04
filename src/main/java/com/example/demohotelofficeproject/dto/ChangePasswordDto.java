package com.example.demohotelofficeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@EqualsAndHashCode
public class ChangePasswordDto {
    private String oldPassword;
    private  String newPassword;
}
