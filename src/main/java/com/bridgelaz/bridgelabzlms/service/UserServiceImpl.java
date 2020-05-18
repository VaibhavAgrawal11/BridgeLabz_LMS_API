package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SignUpServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LoginResponse save(UserDTO user) {
        user.setCreator_stamp(LocalDateTime.now());
        user.setCreator_user(user.getFirst_name());
        user.setVerified("yes");
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        User newUser = modelMapper.map(user, User.class);
        userRepository.save(newUser);
        return new LoginResponse(200, "Register successfull");
    }
}
