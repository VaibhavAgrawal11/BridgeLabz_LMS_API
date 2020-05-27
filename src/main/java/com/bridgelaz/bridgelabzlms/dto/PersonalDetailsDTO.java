package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PersonalDetailsDTO {
    private Date birthDate;
    private String parentName;
    private String parentsOccupation;
    private Long parentsMobileNumber;
    private Double parentsAnnualSalary;
    private String localAddress;
    private String permanentAddress;
    private String photoPath;
}
