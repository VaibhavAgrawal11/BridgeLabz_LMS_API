package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import com.bridgelaz.bridgelabzlms.service.HireCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hireCandidate")
public class HireCandidateController {
    @Autowired
    private HireCandidateService hiredCandidateService;

    @PostMapping("/postCandidateList")
    public UserResponse importHiredCandidate(@RequestParam String filePath) throws IOException {
        List hiredCandidate = hiredCandidateService.getHiredCandidate(filePath);
        hiredCandidateService.saveCandidateDetails(hiredCandidate);
        return new UserResponse(200, "Successfully Noted");
    }

    @GetMapping("/getAllCandidates")
    public List getAllHiredCandidate() throws IOException {
        return hiredCandidateService.getAllHiredCandidates();
    }

    @GetMapping("/viewProfile")
    public Optional<HiredCandidateModel> viewCandidateProfile(@RequestParam int id) throws IOException {
        return hiredCandidateService.viewCandidateProfile(id);
    }
}
