package com.bridgelaz.bridgelabzlms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity(name = "Candidate_Documents")
@Data
public class UploadDocumentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private FellowshipCandidateModel candidateId;
    private String docType;
    private String docPath;
    private String status;
    private String creatorUser;
    private LocalDateTime creatorStamp;
}
