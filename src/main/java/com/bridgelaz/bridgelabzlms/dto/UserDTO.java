package com.bridgelaz.bridgelabzlms.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UserDTO {
    @Size(max = 100)
    @Pattern(regexp = "^[A-Z]+[A-Za-z0-9]{1,}$")
    private String first_name;
    @Size(max = 100)
    @Pattern(regexp = "^[A-Z]+[A-Za-z0-9]{1,}$")
    private String last_name;
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*.{8,}$")
    private String password;
    private long contact_number;
    private String Verified;
    private LocalDateTime creator_stamp;
    private String creator_user;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getContact_number() {
        return contact_number;
    }

    public void setContact_number(long contact_number) {
        this.contact_number = contact_number;
    }

    public String getVerified() {
        return Verified;
    }

    public void setVerified(String verified) {
        Verified = verified;
    }

    public LocalDateTime getCreator_stamp() {
        return creator_stamp;
    }

    public void setCreator_stamp(LocalDateTime creator_stamp) {
        this.creator_stamp = creator_stamp;
    }

    public String getCreator_user() {
        return creator_user;
    }

    public void setCreator_user(String creator_user) {
        this.creator_user = creator_user;
    }
}
