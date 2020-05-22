package com.bridgelaz.bridgelabzlms.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Table
@Entity(name = "Hired_Candidate")
@Data
public class HiredCandidateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailId;
    private String hiredCity;
    private String degree;
    private Date hiredDate;
    private Long mobileNumber;
    private Integer permanentPincode;
    private String hiredLab;
    private String attitude;
    private String communicationRemark;
    private String knowledgeRemark;
    private String aggregateRemark;
    private String status;
    private LocalDateTime creatorStamp;
    private String creatorUser;
}
