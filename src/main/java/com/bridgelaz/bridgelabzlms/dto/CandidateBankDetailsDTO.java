package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

@Data
public class CandidateBankDetailsDTO {
    int candidateId;
    String name;
    String accountNumber;
    String isfcCode;
    String panNumber;
    Long aadharNumber;
}
