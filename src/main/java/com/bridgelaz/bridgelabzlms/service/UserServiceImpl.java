package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.configuration.ApplicationConfiguration;
import com.bridgelaz.bridgelabzlms.dto.UserDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.models.User;
import com.bridgelaz.bridgelabzlms.repository.UserRepository;
import com.bridgelaz.bridgelabzlms.request.LoginRequest;
import com.bridgelaz.bridgelabzlms.response.LoginResponse;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.util.RedisUtil;
import com.bridgelaz.bridgelabzlms.util.Token;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static com.bridgelaz.bridgelabzlms.exception.CustomServiceException.ExceptionType.*;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    EntityManager entityManager;
    @Value("${redisKey}")
    private String REDIS_KEY;
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
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtil<Object> redis;
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
        return new UserResponse(newUser
                , ApplicationConfiguration.getMessageAccessor().getMessage("101"));
    }

    /**
     * This method maps the users details in the the inbuilt UserDetails class
     *
     * @param emailId
     * @return UserDetails
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(emailId).orElseThrow(
                () -> new CustomServiceException(NO_SUCH_USER, "Invalid user")
        );
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    /**
     * Takes email address from user and sends mail to reset password again
     *
     * @param emailAddress
     * @return UserResponse
     */
    @Override
    public UserResponse sentEmail(String emailAddress) throws MessagingException, CustomServiceException {
        User user = userRepository.findByEmail(emailAddress).orElseThrow(
                () -> new CustomServiceException
                        (INVALID_EMAIL_ID, "User not found with email"));
        final String token = jwtToken.generatePasswordResetToken(String.valueOf(user.getEmail()));
        String recipientAddress = user.getEmail();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(recipientAddress);
        helper.setText("Hii " + user.getFirstName() + "\n" + " You requested to reset password, if YES then click on link put your new password and NO then ignore");
        helper.setSubject("Password-Reset-Request");
        sender.send(message);
        return new UserResponse(token
                , ApplicationConfiguration.getMessageAccessor().getMessage("103"));
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
            throw new CustomServiceException(USER_DISABLED, e.getMessage());
        } catch (BadCredentialsException e) {
            throw new CustomServiceException(INVALID_CREDENTIALS, e.getMessage());
        }

        final UserDetails userDetails = this.loadUserByUsername(loginRequest.getEmailId());

        final String token = jwtTokenUtil.generateToken(userDetails);

        redis.putMap(REDIS_KEY, userDetails.getUsername(), token);
        return ResponseEntity.ok(new LoginResponse(token
                , ApplicationConfiguration.getMessageAccessor().getMessage("102")));
    }

    /**
     * Takes new password and JWT for user authorization
     *
     * @param password
     * @return UserResponse
     */
    @Override
    public UserResponse resetPassword(String password, String token) throws CustomServiceException {
        String encodedPassword = bcryptEncoder.encode(password);
        if (jwtToken.isTokenExpired(token)) {
            throw new CustomServiceException(INVALID_TOKEN, "Token has Expired");
        }
        String username = jwtToken.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username)
                .map(user1 -> {
                            user1.setPassword(encodedPassword);
                            return user1;
                        }
                ).map(userRepository::save).get();
        if (user.getPassword().equalsIgnoreCase(encodedPassword))
            return new UserResponse(user
                    , ApplicationConfiguration.getMessageAccessor().getMessage("104"));
        return new UserResponse(user
                , ApplicationConfiguration.getMessageAccessor().getMessage("106"));
    }
}
