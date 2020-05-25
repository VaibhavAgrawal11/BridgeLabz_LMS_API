package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.ResetPassword;
import com.bridgelaz.bridgelabzlms.dto.UserDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.request.LoginRequest;
import com.bridgelaz.bridgelabzlms.response.LoginResponse;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/user")
/**
 *User controller takes service of IUserService interface
 */
public class UserController {
    @Autowired
    private IUserService IUserService;

    @GetMapping("/")
    public String displayHomePage() {
        return "Welcome to Bridgelabz LMS API development project.";
    }

    /**
     * Take user details to register in database
     *
     * @param user
     * @return UserResponse
     * @throws Exception
     */
    @PostMapping(value = "/register")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserDTO user) throws Exception {
        return new ResponseEntity<>(IUserService.save(user), HttpStatus.CREATED);
    }

    /**
     * Takes user name and password to login in application
     *
     * @param loginRequest
     * @return LoginResponse
     */
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return new ResponseEntity(IUserService.login(loginRequest), HttpStatus.OK);
    }

    /**
     * Takes email address from user and sends mail to reset password again
     *
     * @param emailAddress
     * @return UserResponse
     */
    @GetMapping("/forgotpassword")
    public ResponseEntity<UserResponse> requestResetPassword(@Valid @RequestParam @Email String emailAddress) throws AddressException, MessagingException, CustomServiceException {
        return new ResponseEntity<>(IUserService.sentEmail(emailAddress), HttpStatus.ACCEPTED);
    }

    /**
     * Takes new password and JWT for user authorization
     *
     * @param resetPassword
     * @return UserResponse
     */
    @PutMapping("/resetpassword")
    public ResponseEntity<UserResponse> resetPassword(@Valid @RequestBody ResetPassword resetPassword) throws CustomServiceException {
        return new ResponseEntity<>(IUserService.resetPassword(resetPassword.getPassword(), resetPassword.getToken())
                , HttpStatus.ACCEPTED);
    }
}
