package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.UserDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.request.LoginRequest;
import com.bridgelaz.bridgelabzlms.response.LoginResponse;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;

public interface IUserService extends UserDetailsService {
    public UserResponse save(UserDTO userDto);

    UserResponse resetPassword(String password, String token) throws CustomServiceException;

    UserResponse sentEmail(String emailAddress) throws MessagingException, CustomServiceException;

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws Exception;
}
