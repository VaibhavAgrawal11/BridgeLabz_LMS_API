package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.service.IFellowshipCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
