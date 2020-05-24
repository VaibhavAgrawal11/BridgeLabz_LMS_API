package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.models.CandidateBankDetailsModel;
import com.bridgelaz.bridgelabzlms.models.FellowshipCandidateModel;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import com.bridgelaz.bridgelabzlms.models.User;
import com.bridgelaz.bridgelabzlms.repository.CandidateBankDetailsRepository;
import com.bridgelaz.bridgelabzlms.repository.FellowshipCandidateRepository;
import com.bridgelaz.bridgelabzlms.repository.HiredCandidateRepository;
import com.bridgelaz.bridgelabzlms.repository.UserRepository;
import com.bridgelaz.bridgelabzlms.util.Token;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token));
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
        return new UserResponse(200, "Successfully Onboard");
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
    public UserResponse updateCandidateBankInfo(CandidateBankDetailsDTO candidateBankDetailsDTO, String token) {
        CandidateBankDetailsModel candidateBankDetailsModel =
                modelMapper.map(candidateBankDetailsDTO, CandidateBankDetailsModel.class);
        candidateBankDetailsModel.setIsAadharNumberVerified("No");
        candidateBankDetailsModel.setIsAccountNumberVerified("No");
        candidateBankDetailsModel.setIsPanNumberVerified("No");
        candidateBankDetailsModel.setIsIsfcCodeVerified("No");
        candidateBankDetailsModel.setCreatorStamp(LocalDateTime.now());
        User user = userRepository.findByEmail(jwtToken.getUsernameFromToken(token));
        candidateBankDetailsModel.setCreatorUser(user.getCreatorUser());
        bankDetailsRepository.save(candidateBankDetailsModel);

        //Set bamk info status as updated
        FellowshipCandidateModel model = fellowshipCandidateRepository
                .findById(candidateBankDetailsModel.getCandidateId());
        model.setBankInformation("Updated");
        fellowshipCandidateRepository.save(model);
        return new UserResponse(200, "Successfully updated bank info");
    }
}
