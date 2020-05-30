package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.EducationalInfoDTO;
import com.bridgelaz.bridgelabzlms.dto.PersonalDetailsDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IFellowshipCandidate {
    UserResponse onboardAcceptedCandidates(String token);

    int getCandidateCount();

    UserResponse updateCandidateBankInfo(CandidateBankDetailsDTO candidateId, String token) throws CustomServiceException;

    UserResponse notifyCandidate() throws MessagingException;

    UserResponse updateCandidatePersonalInfo(PersonalDetailsDTO personalDetails, int candidateId) throws CustomServiceException;

    UserResponse updateCandidateEducationalInfo(EducationalInfoDTO educationalInfo, String token) throws CustomServiceException;

    UserResponse upload(MultipartFile file, Integer id) throws CustomServiceException, IOException;
}
