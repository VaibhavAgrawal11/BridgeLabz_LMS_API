package com.bridgelaz.bridgelabzlms.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity(name = "Hired_Candidate")
@Data
public class HiredCandidateModel {
    @Id
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailId;
    private String hiredCity;
    private String degree;
    private Date hiredDate;
    private String mobileNumber;
    private String permanentPincode;
    private String hiredLab;
    private String attitude;
    private String communicationRemark;
    private String knowledgeRemark;
    private String aggregateRemark;
    private String status;
    private Date creatorStamp;
    private String creatorUser;
}
