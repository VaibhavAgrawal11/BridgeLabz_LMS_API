package com.bridgelaz.bridgelabzlms.dto;

import com.bridgelaz.bridgelabzlms.models.FellowshipCandidateModel;
import lombok.Data;

@Data
public class UploadDocumentDTO {
    private FellowshipCandidateModel candidateId;
    private String docType;
    private String docPath;
    private String status;
}
