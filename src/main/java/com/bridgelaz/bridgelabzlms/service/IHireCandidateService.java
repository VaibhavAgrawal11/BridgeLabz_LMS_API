package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

public interface IHireCandidateService {
    UserResponse viewCandidateProfile(Integer id) throws CustomServiceException;

    List getAllHiredCandidates() throws CustomServiceException;

    UserResponse dropHireCandidateInDataBase(MultipartFile filePath, String token) throws MessagingException, CustomServiceException;

    UserResponse updateStatus(String candidateResponse, String emailId) throws CustomServiceException;
}
