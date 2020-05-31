package com.bridgelaz.bridgelabzlms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity(name = "candidate_qualification")
@Data
public class EducationalInfoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private FellowshipCandidateModel candidateId;
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

