package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.CandidateBankDetailsDTO;
import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.service.IFellowshipCandidate;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserResponse onboard(@RequestParam String token) {
        return fellowshipCandidateService.onboardAcceptedCandidates(token);
    }

    /**
     * Return total number of candidates present in database
     *
     * @return count of candidates
     */
    @GetMapping("/count")
    public int getCandidateCount() {
        return fellowshipCandidateService.getCandidateCount();
    }

    /**
     * Update Candidate bank details
     *
     * @param candidateBankDetailsDTO
     * @param token
     * @return
     */
    @PostMapping("/bankinfo")
    public UserResponse updateCandidateBankInfo(@RequestBody CandidateBankDetailsDTO candidateBankDetailsDTO,
                                                @RequestParam String token) {
        return fellowshipCandidateService
                .updateCandidateBankInfo(candidateBankDetailsDTO, token);
    }
}
