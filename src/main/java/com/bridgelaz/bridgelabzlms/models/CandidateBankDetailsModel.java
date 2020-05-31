package com.bridgelaz.bridgelabzlms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table
@Entity(name = "CandidateBankDetails")
public class CandidateBankDetailsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private FellowshipCandidateModel candidateId;
    private String name;
    private String accountNumber;
    private String isAccountNumberVerified;
    private String isfcCode;
    private String isIsfcCodeVerified;
    private String panNumber;
    private String isPanNumberVerified;
    private Long aadharNumber;
    private String isAadharNumberVerified;
    private String creatorUser;
    private LocalDateTime creatorStamp;
}
