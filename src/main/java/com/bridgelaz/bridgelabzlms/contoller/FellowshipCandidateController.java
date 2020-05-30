package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.EducationalInfoDTO;
import com.bridgelaz.bridgelabzlms.dto.PersonalDetailsDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.service.IFellowshipCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/fellow")
public class FellowshipCandidateController {
    @Autowired
    IFellowshipCandidate fellowshipCandidateService;

    /**
     * Onboard hired candidates to fellowship plan
     *
     * @param token
     * @return user response
     */
    @PostMapping("/onboard")
    public ResponseEntity<UserResponse> onboard(@RequestParam String token) {
        return new ResponseEntity<>(fellowshipCandidateService.onboardAcceptedCandidates(token)
                , HttpStatus.MULTI_STATUS);
    }

    /**
     * Return total number of candidates present in database
     *
     * @return count of candidates
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getCandidateCount() {

        return new ResponseEntity<>(fellowshipCandidateService.getCandidateCount()
                , HttpStatus.OK);
    }

    /**
     * Send jo offer notification to all fellowship students
     *
     * @return UserResponse
     * @throws MessagingException
     */
    @PostMapping("/job")
    public ResponseEntity<UserResponse> sendJobNotification() throws MessagingException {
        return new ResponseEntity<>(fellowshipCandidateService.notifyCandidate()
                , HttpStatus.ACCEPTED);
    }

    /**
     * Updates candidate personal information
     *
     * @param detailsDTO
     * @param id
     * @return UserResponse
     * @throws CustomServiceException
     */
    @PutMapping("/personalinfo/{id}")
    public ResponseEntity<UserResponse> updateCandidatePersonalInfo(@RequestBody PersonalDetailsDTO detailsDTO,
                                                                    @PathVariable("id") int id) throws CustomServiceException {
        return new ResponseEntity<>(fellowshipCandidateService.updateCandidatePersonalInfo(detailsDTO, id)
                , HttpStatus.CREATED);
    }

    /**
     * Update Candidate bank details
     *
     * @param candidateBankDetailsDTO
     * @param token
     * @return
     */
    @PostMapping("/bankinfo")
    public ResponseEntity<UserResponse> updateCandidateBankInfo(@RequestBody CandidateBankDetailsDTO candidateBankDetailsDTO,
                                                                @RequestParam String token) throws CustomServiceException {
        return new ResponseEntity<>(fellowshipCandidateService.updateCandidateBankInfo(candidateBankDetailsDTO, token)
                , HttpStatus.CREATED);
    }

    /**
     * Update candidate educational details
     *
     * @param educationalInfo
     * @param token
     * @return UserResponse
     * @throws CustomServiceException
     */
    @PostMapping("/eduinfo")
    public ResponseEntity<UserResponse> updateCandidateEducationalInfo(@RequestBody EducationalInfoDTO educationalInfo,
                                                                       @RequestParam String token) throws CustomServiceException {
        return new ResponseEntity<>(fellowshipCandidateService.updateCandidateEducationalInfo(educationalInfo, token)
                , HttpStatus.CREATED);
    }

    @PostMapping(value = "/doc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> uploadDocument(@RequestParam("file") MultipartFile file,
                                                       @RequestParam(value = "id") Integer id) throws IOException, CustomServiceException {
        return new ResponseEntity<>(fellowshipCandidateService.upload(file, id), HttpStatus.OK);
    }

}
