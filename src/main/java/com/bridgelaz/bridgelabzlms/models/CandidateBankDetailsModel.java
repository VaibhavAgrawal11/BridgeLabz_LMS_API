package com.bridgelaz.bridgelabzlms.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table
@Entity(name = "CandidateBankDetails")
public class CandidateBankDetailsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    int candidateId;
    String name;
    String accountNumber;
    String isAccountNumberVerified;
    String isfcCode;
    String isIsfcCodeVerified;
    String panNumber;
    String isPanNumberVerified;
    Long aadharNumber;
    String isAadharNumberVerified;
    String creatorUser;
    LocalDateTime creatorStamp;
}
