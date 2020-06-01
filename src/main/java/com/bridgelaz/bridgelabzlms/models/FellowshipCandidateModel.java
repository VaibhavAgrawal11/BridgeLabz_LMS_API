package com.bridgelaz.bridgelabzlms.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Table()
@Entity(name = "FellowshipCandidate")
@Data
@NoArgsConstructor
public class FellowshipCandidateModel {
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
    private Date birthDate;
    private String isBirthDateVerified;
    private String parentName;
    private String parentsOccupation;
    private Long parentsMobileNumber;
    private Double parentsAnnualSalary;
    private String localAddress;
    private String permanentAddress;
    private String photoPath;
    private String hiredLab;
    private String attitude;
    private String communicationRemark;
    private String knowledgeRemark;
    private String aggregateRemark;
    private LocalDateTime creatorStamp;
    private String creatorUser;
    private String bankInformation;
    private String educationalInformation;

    public FellowshipCandidateModel(int id) {
        this.id = id;
    }
}
