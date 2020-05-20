package com.bridgelaz.bridgelabzlms.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity(name = "UserDetails")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private long contactNumber;
    private String Verified;
    private LocalDateTime creatorStamp;
    private String creatorUser;
}