package com.bridgelaz.bridgelabzlms.dto;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 4304137534438900456L;
    private String emailId;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.setEmailId(username);
        this.setPassword(password);
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
