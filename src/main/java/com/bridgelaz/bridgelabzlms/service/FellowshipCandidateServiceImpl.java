package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.configuration.ApplicationConfiguration;
import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.EducationalInfoDTO;
import com.bridgelaz.bridgelabzlms.dto.PersonalDetailsDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.models.*;
import com.bridgelaz.bridgelabzlms.repository.*;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.util.Token;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.bridgelaz.bridgelabzlms.exception.CustomServiceException.ExceptionType.DATA_NOT_FOUND;
import static com.bridgelaz.bridgelabzlms.exception.CustomServiceException.ExceptionType.INVALID_ID;

@Service
public class FellowshipCandidateServiceImpl implements IFellowshipCandidate {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HiredCandidateRepository hiredCandidateRepository;
    @Autowired
    FellowshipCandidateRepository fellowshipCandidateRepository;
    @Autowired
    CandidateBankDetailsRepository bankDetailsRepository;
    @Autowired
    EducationalInfoRepository educationalInfoRepository;
    @Autowired
    Token jwtToken;
    @Autowired
    JavaMailSender sender;

    @Value("${upload.path}")
    private String path;


    private final Path fileLocation = java.nio.file.Paths.get(path);

    /**
     * Take data from hire candidate table and drop in fellowship candidate table
     * If the status of the candidate is accepted the only he/she is transferred
     *
     * @param token
     * @return user response
     */
    @Override
    public UserResponse onboardAcceptedCandidates(String token) {
        List<HiredCandidateModel> candidates = hiredCandidateRepository.findAll();
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token)).get();
        for (HiredCandidateModel candidate : candidates) {
            if (candidate.getStatus().equals("Accepted")) {
                FellowshipCandidateModel fellowshipCandidate = modelMapper
                        .map(candidate, FellowshipCandidateModel.class);
                fellowshipCandidate.setCreatorUser(user.getCreatorUser());
                fellowshipCandidate.setCreatorStamp(LocalDateTime.now());
                fellowshipCandidate.setBankInformation("Pending");
                fellowshipCandidate.setEducationalInformation("Pending");
                fellowshipCandidateRepository.save(fellowshipCandidate);
            }
        }
        return new UserResponse(true
                , ApplicationConfiguration.getMessageAccessor().getMessage("110"));
    }

    /**
     * Return total number of candidates present in database
     *
     * @return count of candidates
     */
    @Override
    public int getCandidateCount() {
        return fellowshipCandidateRepository.findAll().size();
    }

    /**
     * Update Candidate bank details
     *
     * @param candidateBankDetailsDTO
     * @param token
     * @return
     */
    @Override
    public UserResponse updateCandidateBankInfo(CandidateBankDetailsDTO candidateBankDetailsDTO, String token) throws CustomServiceException {
        CandidateBankDetailsModel candidateBankDetailsModel =
                modelMapper.map(candidateBankDetailsDTO, CandidateBankDetailsModel.class);
        candidateBankDetailsModel.setIsAadharNumberVerified("No");
        candidateBankDetailsModel.setIsAccountNumberVerified("No");
        candidateBankDetailsModel.setIsPanNumberVerified("No");
        candidateBankDetailsModel.setIsIsfcCodeVerified("No");
        candidateBankDetailsModel.setCreatorStamp(LocalDateTime.now());
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token)).get();
        candidateBankDetailsModel.setCreatorUser(user.getCreatorUser());
        bankDetailsRepository.save(candidateBankDetailsModel);

        //Set bank info status as updated
        FellowshipCandidateModel model = fellowshipCandidateRepository
                .findById(candidateBankDetailsModel.getCandidateId())
                .orElseThrow(() -> new CustomServiceException(INVALID_ID, "No such id present"));
        model.setBankInformation("Updated");
        fellowshipCandidateRepository.save(model);
        return new UserResponse(bankDetailsRepository
                , ApplicationConfiguration.getMessageAccessor().getMessage("111"));
    }

    /**
     * Send jo offer notification to all fellowship students
     *
     * @return
     * @throws MessagingException
     */
    @Override
    public UserResponse notifyCandidate() throws MessagingException {
        List<FellowshipCandidateModel> fellowshipCandidateList = fellowshipCandidateRepository.findAll();
        for (FellowshipCandidateModel candidate : fellowshipCandidateList) {
            String emailId = candidate.getEmailId();
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(emailId);
            helper.setText("Hello " + candidate.getFirstName() + " " + candidate.getLastName() + "," + "\n\n" +
                    "Congratulations, you have accepted our 4 months fellowship course," +
                    "\n This is the job notification mail");
            helper.setSubject("Job Offer Notification");
            sender.send(message);
        }
        return new UserResponse(true, ApplicationConfiguration.getMessageAccessor().getMessage("112"));
    }

    /**
     * Updates candidate personal information
     *
     * @param personalDetails
     * @param candidateId
     * @return UserResponse
     * @throws CustomServiceException
     */
    @Override
    public UserResponse updateCandidatePersonalInfo(PersonalDetailsDTO personalDetails, int candidateId) throws CustomServiceException {
        FellowshipCandidateModel candidateModel = fellowshipCandidateRepository.findById(candidateId)
                .orElseThrow(() -> new CustomServiceException(INVALID_ID, "Id is not present in data base."));
        modelMapper.map(personalDetails, candidateModel);
        candidateModel.setIsBirthDateVerified("No");
        fellowshipCandidateRepository.save(candidateModel);
        //Displaying the updated changes
        return new UserResponse(candidateModel, ApplicationConfiguration.getMessageAccessor().getMessage("113"));
    }

    /**
     * Update candidate bank details
     *
     * @param educationalInfo
     * @param token
     * @return UserResponse
     * @throws CustomServiceException
     */
    @Override
    public UserResponse updateCandidateEducationalInfo(EducationalInfoDTO educationalInfo, String token) throws CustomServiceException {
        FellowshipCandidateModel candidateModel = fellowshipCandidateRepository.findById(educationalInfo
                .getCandidateId())
                .orElseThrow(() -> new CustomServiceException(INVALID_ID, "Invalid candidate id"));
        EducationalInfoModel educationalInfoModel = modelMapper.map(educationalInfo, EducationalInfoModel.class);
        educationalInfoModel.setIsDegreeNameVerified("No");
        educationalInfoModel.setIsEmployeeDisciplineVerified("No");
        educationalInfoModel.setIsFinalYearPerVerified("No");
        educationalInfoModel.setIsOtherTrainingVerified("No");
        educationalInfoModel.setIsPassingYearVerified("No");
        educationalInfoModel.setIsTrainingDurationMonthVerified("No");
        educationalInfoModel.setIsTrainingInstituteVerified("No");
        educationalInfoModel.setCreatorStamp(LocalDateTime.now());
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token)).get();
        educationalInfoModel.setCreatorUser(user.getCreatorUser());
        educationalInfoRepository.save(educationalInfoModel);
        candidateModel.setEducationalInformation("Updated");
        fellowshipCandidateRepository.save(candidateModel);
        return new UserResponse(educationalInfoModel, ApplicationConfiguration.getMessageAccessor().getMessage("114"));
    }

    @Override
    public UserResponse upload(MultipartFile file, Integer id) throws CustomServiceException, IOException {
        fellowshipCandidateRepository.findById(id)
                .orElseThrow(() -> new CustomServiceException(DATA_NOT_FOUND, "Data not found"));
        if (file.isEmpty())
            throw new CustomServiceException(DATA_NOT_FOUND, "Failed to store empty file");
        String uniqueId = UUID.randomUUID().toString();
        Files.copy(file.getInputStream(), fileLocation.resolve(uniqueId),
                StandardCopyOption.REPLACE_EXISTING);
        UploadDocumentModel uploadDocument = new UploadDocumentModel();
        uploadDocument.setCandidateId(id);
        uploadDocument.setDocPath(uniqueId);
        uploadDocument.setStatus("pending");
        return new UserResponse(uploadDocument, ApplicationConfiguration.getMessageAccessor().getMessage("115"));
    }
}
