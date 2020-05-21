package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.LoginRequest;
import com.bridgelaz.bridgelabzlms.dto.LoginResponse;
import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.dto.UserDTO;
import com.bridgelaz.bridgelabzlms.models.User;
import com.bridgelaz.bridgelabzlms.repository.UserRepository;
import com.bridgelaz.bridgelabzlms.util.Token;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Token jwtToken;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Token jwtTokenUtil;

    /**
     * This method maps the users details in the the database
     *
     * @param user
     * @return UserResponse
     */
    @Override
    public UserResponse save(UserDTO user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        User newUser = modelMapper.map(user, User.class);
        newUser.setCreatorStamp(LocalDateTime.now());
        newUser.setCreatorUser(newUser.getFirstName());
        newUser.setVerified("yes");
        userRepository.save(newUser);
        return new UserResponse(200, "successfully Registered");
    }

    /**
     * This method maps the users details in the the inbuilt UserDetails class
     *
     * @param emailId
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        User user = (User) userRepository.findByEmail(emailId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + emailId);
        }
        return new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(),
                new ArrayList<>());
    }

    /**
     * Takes email address from user and sends mail to reset password again
     *
     * @param emailAddress
     * @return UserResponse
     */
    @Override
    public UserResponse sentEmail(String emailAddress) throws MessagingException {
        User user = userRepository.findByEmail(emailAddress);
        final String token = jwtToken.generatePasswordResetToken(String.valueOf(user.getId()));
        String recipientAddress = user.getEmail();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(recipientAddress);
        helper.setText("Hii " + user.getFirstName() + "\n" + " You requested to reset password, if YES then click on link put your new password and NO then ignore");
        helper.setSubject("Password-Reset-Request");
        sender.send(message);
        return new UserResponse(200, token);
    }

    /**
     * Takes user name and password to login in application
     *
     * @param loginRequest
     * @return LoginResponse
     */
    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(),
                            loginRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = this.loadUserByUsername(loginRequest.getEmailId());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    /**
     * Takes new password and JWT for user authorization
     *
     * @param password
     * @return UserResponse
     */
    @Override
    public UserResponse resetPassword(String password, String token) {
        String encodedPassword = bcryptEncoder.encode(password);
        if (jwtToken.isTokenExpired(token)) {
            return new UserResponse(500, "UnSuccessFull");
        }
        String username = jwtToken.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username);
        user.setPassword(encodedPassword);
        User updatedUser = userRepository.save(user);
        if (updatedUser.getPassword().equalsIgnoreCase(encodedPassword))
            return new UserResponse(200, "Successfully updated");
        return new UserResponse(500, "UnSuccessFull");
    }
}
