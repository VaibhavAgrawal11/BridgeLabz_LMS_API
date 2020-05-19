package com.bridgelaz.bridgelabzlms.service;

import java.util.List;

public interface HireCandidateService {
    List getHiredCandidate(String filename);

    void saveCandidateDetails(List hiredCandidate);
}
