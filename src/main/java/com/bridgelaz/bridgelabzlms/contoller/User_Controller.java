package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.*;
import com.bridgelaz.bridgelabzlms.models.User;
import com.bridgelaz.bridgelabzlms.repository.UserRepository;
import com.bridgelaz.bridgelabzlms.service.UserServiceImpl;
import com.bridgelaz.bridgelabzlms.util.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.constraints.Email;

@RestController
public class User_Controller {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Token jwtTokenUtil;

    @RequestMapping({"/hello", "", "/"})
    public String displayHomePage() {
        return "Welcome to Bridgelabz LMS API development project.";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user) throws Exception {
        return ResponseEntity.ok(userService.save(user));
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/user/forgetPassword")
    public UserResponse requestResetPassword(@Valid @RequestParam @Email String emailAddress) throws AddressException, MessagingException {
        User user = userRepository.findByEmail(emailAddress);
        final String token = jwtTokenUtil.generatePasswordResetToken(String.valueOf(user.getId()));
        userService.sentEmail(user, token);
        return new UserResponse(200, token);
    }

    @PutMapping("/reset_password")
    public UserResponse resetPassword(@Valid @RequestBody ResetPassword resetPassword) {
        boolean result = userService.resetPassword(resetPassword.getPassword(), resetPassword.getToken());
        if (result)
            return new UserResponse(200, "Successfully updated");
        return new UserResponse(500, "UnSuccessFull");
    }
}
