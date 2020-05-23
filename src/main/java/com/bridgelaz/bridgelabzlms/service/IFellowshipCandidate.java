package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;

public interface IFellowshipCandidate {
    UserResponse onboardAcceptedCandidates(String token);

    int getCandidateCount();
}
