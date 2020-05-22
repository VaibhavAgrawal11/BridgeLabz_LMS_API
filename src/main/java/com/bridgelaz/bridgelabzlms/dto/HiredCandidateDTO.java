package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HiredCandidateDTO {
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
