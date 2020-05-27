package com.bridgelaz.bridgelabzlms.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity(name = "candidate_qualification")
@Data
public class EducationalInfoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    private int candidateId;
    private String diploma;
    private String degreeName;
    private String isDegreeNameVerified;
    private String employeeDiscipline;
    private String isEmployeeDisciplineVerified;
    private Integer passingYear;
    private String isPassingYearVerified;
    private Double aggrPer;
    private Double finalYearPer;
    private String isFinalYearPerVerified;
    private String trainingInstitute;
    private String isTrainingInstituteVerified;
    private Integer trainingDurationMonth;
    private String isTrainingDurationMonthVerified;
    private String otherTraining;
    private String isOtherTrainingVerified;
    private LocalDateTime creatorStamp;
    private String creatorUser;
}

