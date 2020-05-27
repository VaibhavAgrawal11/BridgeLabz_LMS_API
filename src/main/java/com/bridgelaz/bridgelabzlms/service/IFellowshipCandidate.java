package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.PersonalDetailsDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.response.UserResponse;

import javax.mail.MessagingException;

public interface IFellowshipCandidate {
    UserResponse onboardAcceptedCandidates(String token);

    int getCandidateCount();

    UserResponse updateCandidateBankInfo(CandidateBankDetailsDTO candidateId, String token) throws CustomServiceException;

    UserResponse notifyCandidate() throws MessagingException;

    UserResponse updateCandidatePersonalInfo(PersonalDetailsDTO personalDetails, int candidateId) throws CustomServiceException;
}
