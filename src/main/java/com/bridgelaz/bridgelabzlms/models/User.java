package com.bridgelaz.bridgelabzlms.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table
@Entity(name = "UserDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;
    @NotNull
    private long contactNumber;
    @NotNull
    private String Verified;
    @NotNull
    private LocalDateTime creatorStamp;
    @NotNull
    private String creatorUser;
}