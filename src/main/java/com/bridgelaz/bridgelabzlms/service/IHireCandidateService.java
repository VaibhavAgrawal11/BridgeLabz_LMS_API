package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.List;
import java.util.Optional;

public interface IHireCandidateService {
    Optional<HiredCandidateModel> viewCandidateProfile(Integer id);

    List getAllHiredCandidates();

    UserResponse dropHireCandidateInDataBase(String filePath);
}
