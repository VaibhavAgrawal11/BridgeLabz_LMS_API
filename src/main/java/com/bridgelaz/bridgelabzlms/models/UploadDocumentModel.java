package com.bridgelaz.bridgelabzlms.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity(name = "Candidate_Documents")
@Data
public class UploadDocumentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer candidateId;
    private String docType;
    private String docPath;
    private String status;
    private String creatorUser;
    private LocalDateTime creatorStamp;
}
