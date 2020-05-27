package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

@Data
public class EducationalInfoDTO {
    private int candidateId;
    private String diploma;
    private String degreeName;
    private String employeeDiscipline;
    private Integer passingYear;
    private Double aggrPer;
    private Double finalYearPer;
    private String trainingInstitute;
    private Integer trainingDurationMonth;
    private String otherTraining;
}
