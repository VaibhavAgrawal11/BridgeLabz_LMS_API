package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.configuration.ApplicationConfiguration;
import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.EducationalInfoDTO;
import com.bridgelaz.bridgelabzlms.dto.PersonalDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.RabbitMqDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.models.*;
import com.bridgelaz.bridgelabzlms.repository.*;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.util.CandidateResponse;
import com.bridgelaz.bridgelabzlms.util.RabbitMq;
import com.bridgelaz.bridgelabzlms.util.Token;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bridgelaz.bridgelabzlms.exception.CustomServiceException.ExceptionType.DATA_NOT_FOUND;
import static com.bridgelaz.bridgelabzlms.exception.CustomServiceException.ExceptionType.INVALID_ID;
import static com.bridgelaz.bridgelabzlms.util.DocumentStatus.PENDING;
import static com.bridgelaz.bridgelabzlms.util.DocumentStatus.UPDATED;
import static com.bridgelaz.bridgelabzlms.util.Verification.NOT_VERIFIED;

@Service
public class FellowshipCandidateServiceImpl implements IFellowshipCandidate {
    private final Path fileLocation = java.nio.file.Paths.get("/home/vaibhav/Desktop/Spring/BridgeLabzLMS/src/main/resources/");
    private final String verification = String.valueOf(NOT_VERIFIED);
    private final String updated = String.valueOf(UPDATED);
    private final String pending = String.valueOf(PENDING);
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
    UploadDocumentRepository documentRepository;
    @Autowired
    Token jwtToken;
    @Autowired
    JavaMailSender sender;
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    RabbitMqDTO rabbitMqDTO;
    @Autowired
    RabbitMq rabbitMailSender;

    String accepted = String.valueOf(CandidateResponse.ACCEPTED);

    /**
     * Take data from hire candidate table and drop in fellowship candidate table
     * If the status of the candidate is accepted then only he/she is transferred
     *
     * @param token
     * @return user response
     */
    @Override
    public UserResponse onboardAcceptedCandidates(String token) {
        List<HiredCandidateModel> candidates = hiredCandidateRepository.findAll();
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token)).get();
        for (HiredCandidateModel candidate : candidates) {
            if (candidate.getStatus().equals(accepted)) {
                FellowshipCandidateModel fellowshipCandidate = modelMapper
                        .map(candidate, FellowshipCandidateModel.class);
                fellowshipCandidate.setCreatorUser(user.getCreatorUser());
                fellowshipCandidate.setCreatorStamp(LocalDateTime.now());
                fellowshipCandidate.setBankInformation(pending);
                fellowshipCandidate.setEducationalInformation(pending);
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
     * Update Candidate bank details, by taking information by user and mapping it with database
     *
     * @param candidateBankDetailsDTO
     * @param token
     * @return UserResponse
     */
    @Override
    public UserResponse updateCandidateBankInfo(CandidateBankDetailsDTO candidateBankDetailsDTO, String token) throws CustomServiceException {
        CandidateBankDetailsModel candidateBankDetailsModel =
                modelMapper.map(candidateBankDetailsDTO, CandidateBankDetailsModel.class);
        candidateBankDetailsModel.setIsAadharNumberVerified(verification);
        candidateBankDetailsModel.setIsAccountNumberVerified(verification);
        candidateBankDetailsModel.setIsPanNumberVerified(verification);
        candidateBankDetailsModel.setIsIsfcCodeVerified(verification);
        candidateBankDetailsModel.setCreatorStamp(LocalDateTime.now());
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token)).get();
        candidateBankDetailsModel.setCreatorUser(user.getCreatorUser());
        //Set bank info status as updated
        FellowshipCandidateModel model = fellowshipCandidateRepository
                .findById(candidateBankDetailsModel.getCandidateId().getId())
                .orElseThrow(() -> new CustomServiceException(INVALID_ID, "No such id present"));
        model.setBankInformation(updated);
        fellowshipCandidateRepository.save(model);
        bankDetailsRepository.save(candidateBankDetailsModel);

        return new UserResponse(bankDetailsRepository
                , ApplicationConfiguration.getMessageAccessor().getMessage("111"));
    }

    /**
     * Send job offer notification to all fellowship students
     *
     * @return
     * @throws MessagingException
     */
    @Override
    public UserResponse notifyCandidate() throws MessagingException {
        List<FellowshipCandidateModel> fellowshipCandidateList = fellowshipCandidateRepository.findAll();
        for (FellowshipCandidateModel candidate : fellowshipCandidateList) {
            String emailId = candidate.getEmailId();
            rabbitMqDTO.setTo(emailId);
            rabbitMqDTO.setSubject("Job Offer Notification");
            rabbitMqDTO.setBody("Hello " + candidate.getFirstName() + " " + candidate.getLastName() + "," + "\n\n" +
                    "Congratulations, you have accepted our 4 months fellowship course," +
                    "\n This is the job notification mail");
            rabbitMailSender.sendMessageToQueue(rabbitMqDTO);
            rabbitMailSender.RabbitSend(rabbitMqDTO);
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
        candidateModel.setIsBirthDateVerified(verification);
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
                .getCandidateId().getId())
                .orElseThrow(() -> new CustomServiceException(INVALID_ID, "Invalid candidate id"));
        EducationalInfoModel educationalInfoModel = modelMapper.map(educationalInfo, EducationalInfoModel.class);
        educationalInfoModel.setIsDegreeNameVerified(verification);
        educationalInfoModel.setIsEmployeeDisciplineVerified(verification);
        educationalInfoModel.setIsFinalYearPerVerified(verification);
        educationalInfoModel.setIsOtherTrainingVerified(verification);
        educationalInfoModel.setIsPassingYearVerified(verification);
        educationalInfoModel.setIsTrainingDurationMonthVerified(verification);
        educationalInfoModel.setIsTrainingInstituteVerified(verification);
        educationalInfoModel.setCreatorStamp(LocalDateTime.now());
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token)).get();
        educationalInfoModel.setCreatorUser(user.getCreatorUser());
        educationalInfoRepository.save(educationalInfoModel);
        candidateModel.setEducationalInformation(updated);
        fellowshipCandidateRepository.save(candidateModel);
        return new UserResponse(educationalInfoModel, ApplicationConfiguration.getMessageAccessor().getMessage("114"));
    }

    @Override
    public UserResponse upload(MultipartFile file, String token, int id) throws CustomServiceException, IOException {
        fellowshipCandidateRepository.findById(id)
                .orElseThrow(() -> new CustomServiceException(DATA_NOT_FOUND, "Data not found"));
        if (file.isEmpty())
            throw new CustomServiceException(DATA_NOT_FOUND, "Failed to store empty file");
        UploadDocumentModel uploadDocument = new UploadDocumentModel();
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token)).get();
        uploadDocument.setCreatorUser(user.getCreatorUser());
        uploadDocument.setDocPath(uploadFile(file));
        uploadDocument.setStatus(verification);
        uploadDocument.setDocType(file.getContentType());
        uploadDocument.setCandidateId(new FellowshipCandidateModel(id));
        documentRepository.save(uploadDocument);
        return new UserResponse(uploadDocument, ApplicationConfiguration.getMessageAccessor().getMessage("115"));
    }

    private String uploadFile(MultipartFile file) throws IOException {
        File fileToUpload = convertMultipartToFile(file);
        Map uploadResult = cloudinary.uploader().upload(fileToUpload, ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }
}
