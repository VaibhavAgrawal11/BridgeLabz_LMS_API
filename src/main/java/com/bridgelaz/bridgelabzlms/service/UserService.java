package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.LoginDTO;
import com.bridgelaz.bridgelabzlms.dto.UserDTO;

public interface UserService {
    public LoginDTO save(UserDTO userDto);
}
