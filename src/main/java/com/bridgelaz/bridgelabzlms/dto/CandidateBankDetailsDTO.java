package com.bridgelaz.bridgelabzlms.dto;

import com.bridgelaz.bridgelabzlms.models.FellowshipCandidateModel;
import lombok.Data;

@Data
public class CandidateBankDetailsDTO {
    FellowshipCandidateModel candidateId;
    String name;
    String accountNumber;
    String isfcCode;
    String panNumber;
    Long aadharNumber;
}
