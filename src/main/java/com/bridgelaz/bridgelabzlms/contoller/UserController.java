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
public class UserController {
    @Autowired
    private IUserService IUserService;


    @RequestMapping({"/hello", "", "/"})
    public String displayHomePage() {
        return "Welcome to Bridgelabz LMS API development project.";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user) throws Exception {
        return ResponseEntity.ok(IUserService.save(user));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return IUserService.login(loginRequest);
    }

    @GetMapping("/forgetPassword")
    public UserResponse requestResetPassword(@Valid @RequestParam @Email String emailAddress) throws AddressException, MessagingException {
        return IUserService.sentEmail(emailAddress);
    }

    @PutMapping("/resetPassword")
    public UserResponse resetPassword(@Valid @RequestBody ResetPassword resetPassword) {
        boolean result = IUserService.resetPassword(resetPassword.getPassword(), resetPassword.getToken());
        if (result)
            return new UserResponse(200, "Successfully updated");
        return new UserResponse(500, "UnSuccessFull");
    }
}
