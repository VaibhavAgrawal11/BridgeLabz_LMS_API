package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;

import java.util.List;
import java.util.Optional;

public interface HireCandidateService {
    List getHiredCandidate(String filename);

    void saveCandidateDetails(List hiredCandidate);

    Optional<HiredCandidateModel> viewCandidateProfile(Integer id);

    List getAllHiredCandidates();
}
