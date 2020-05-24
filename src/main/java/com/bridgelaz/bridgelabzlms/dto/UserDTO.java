package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDTO {
    @Size(max = 100)
    @Pattern(regexp = "^[A-Z]+[A-Za-z0-9]{1,}$")
    private String firstName;
    @Size(max = 100)
    @Pattern(regexp = "^[A-Z]+[A-Za-z0-9]{1,}$")
    private String lastName;
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*.{8,}$")
    private String password;
    private long contactNumber;
}
