package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.*;
import com.bridgelaz.bridgelabzlms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/user")
/*
 *User controller takes service of IUserService interface
 * */
public class UserController {
    @Autowired
    private IUserService IUserService;

    @GetMapping("/")
    public String displayHomePage() {
        return "Welcome to Bridgelabz LMS API development project.";
    }

    /*
     * Take user details to register in database
     * @param UserDTO
     * @return UserResponse
     * */
    @PostMapping(value = "/register")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserDTO user) throws Exception {
        return ResponseEntity.ok(IUserService.save(user));
    }

    /*
     * Takes user name and password to login in application
     * @param LoginRequest
     * @return LoginResponse
     * */
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return IUserService.login(loginRequest);
    }

    /*
     * Takes email address from user and sends mail to reset password again
     * @param email address
     * @return UserResponse
     * */
    @GetMapping("/forgotpassword")
    public UserResponse requestResetPassword(@Valid @RequestParam @Email String emailAddress) throws AddressException, MessagingException {
        return IUserService.sentEmail(emailAddress);
    }

    /*
     * Takes new password and JWT for user authorization
     * @param ResentPassword
     * @return UserResponse
     * */
    @PutMapping("/resetpassword")
    public UserResponse resetPassword(@Valid @RequestBody ResetPassword resetPassword) {
        return IUserService.resetPassword(resetPassword.getPassword(), resetPassword.getToken());
    }
}
