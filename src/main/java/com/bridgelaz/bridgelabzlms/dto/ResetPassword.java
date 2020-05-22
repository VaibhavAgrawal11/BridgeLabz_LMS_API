package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ResetPassword {
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*.{8,}$")
    private String password;
    private String token;
}
