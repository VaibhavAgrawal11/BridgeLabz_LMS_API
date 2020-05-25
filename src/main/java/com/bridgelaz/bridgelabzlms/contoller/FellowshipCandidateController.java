package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.service.IFellowshipCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Update Candidate bank details
     *
     * @param candidateBankDetailsDTO
     * @param token
     * @return
     */
    @PostMapping("/bankinfo")
    public ResponseEntity<UserResponse> updateCandidateBankInfo(@RequestBody CandidateBankDetailsDTO candidateBankDetailsDTO,
                                                                @RequestParam String token) throws CustomServiceException {
        return new ResponseEntity<>(fellowshipCandidateService
                .updateCandidateBankInfo(candidateBankDetailsDTO, token)
                , HttpStatus.CREATED);
    }
}
