package com.bridgelaz.bridgelabzlms.dto;

import lombok.Data;

@Data
public class UploadDocumentDTO {
    private Integer candidateId;
    private String docType;
    private String docPath;
    private String status;
}
