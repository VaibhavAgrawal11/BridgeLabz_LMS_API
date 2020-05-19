package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.service.HiredCandidateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class HireCandidateController {
    @Autowired
    private HiredCandidateServiceImpl hiredCandidateService;

    @PostMapping("/hiredCandidate/postCandidateList")
    public UserResponse importHiredCandidate(@RequestParam String filePath) throws IOException {
        List hiredCandidate = hiredCandidateService.getHiredCandidate(filePath);
        hiredCandidateService.saveCandidateDetails(hiredCandidate);
        return new UserResponse(200, "Successfully Noted");
    }

    @GetMapping("/hiredCandidate/getAllCandidates")
    public List getAllHiredCandidate() throws IOException {
        return hiredCandidateService.getAllHiredCandidates();
    }
}
