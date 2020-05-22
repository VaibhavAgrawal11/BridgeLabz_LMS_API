package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IHireCandidateService {
    Optional<HiredCandidateModel> viewCandidateProfile(Integer id);

    List getAllHiredCandidates();

    UserResponse dropHireCandidateInDataBase(MultipartFile filePath);
}
