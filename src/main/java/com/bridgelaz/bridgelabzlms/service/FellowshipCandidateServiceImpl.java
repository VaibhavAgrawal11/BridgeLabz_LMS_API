package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.configuration.ApplicationConfiguration;
import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.models.CandidateBankDetailsModel;
import com.bridgelaz.bridgelabzlms.models.FellowshipCandidateModel;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import com.bridgelaz.bridgelabzlms.models.User;
import com.bridgelaz.bridgelabzlms.repository.CandidateBankDetailsRepository;
import com.bridgelaz.bridgelabzlms.repository.FellowshipCandidateRepository;
import com.bridgelaz.bridgelabzlms.repository.HiredCandidateRepository;
import com.bridgelaz.bridgelabzlms.repository.UserRepository;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.util.Token;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

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
    Token jwtToken;
    @Autowired
    JavaMailSender sender;

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
}
