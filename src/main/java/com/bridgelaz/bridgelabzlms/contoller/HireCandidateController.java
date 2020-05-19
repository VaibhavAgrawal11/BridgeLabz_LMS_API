package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.service.HiredCandidateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("Hired_Candidate")
public class HireCandidateController {
    @Autowired
    private HiredCandidateServiceImpl hiredCandidateService;

    @PostMapping("/import_hired_candidate")
    public UserResponse importHiredCandidate() throws IOException {
        String filename = "./src/main/resources/HiredCandidates.xlsx";
        List hiredCandidate = hiredCandidateService.getHiredCandidate(filename);
        hiredCandidateService.saveCandidateDetails(hiredCandidate);
        return new UserResponse(200, "Successfully imorted");
    }
}
