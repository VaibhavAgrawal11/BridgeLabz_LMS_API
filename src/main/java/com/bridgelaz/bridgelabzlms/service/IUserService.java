package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.LoginRequest;
import com.bridgelaz.bridgelabzlms.dto.LoginResponse;
import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.dto.UserDTO;
import com.bridgelaz.bridgelabzlms.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;

public interface IUserService extends UserDetailsService {
    public UserResponse save(UserDTO userDto);

    boolean resetPassword(String password, String token);

    UserResponse sentEmail(String emailAddress) throws MessagingException;

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws Exception;
}
