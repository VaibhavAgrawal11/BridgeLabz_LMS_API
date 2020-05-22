package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 3631838363180478531L;
    private final String jwttoken;
}
