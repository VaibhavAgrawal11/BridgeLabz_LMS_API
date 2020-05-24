package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import com.bridgelaz.bridgelabzlms.service.IHireCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hirecandidate")
/**
 *
 *Hire candidate controller takes service of IHireCandidateService interface
 */
public class HireCandidateController {
    @Autowired
    private IHireCandidateService hiredCandidateService;

    /**
     * Take excel sheet of candidates and drop the list database
     *
     * @param filePath
     * @return UserResponse
     */
    @PostMapping("/takecandidatelist")
    public UserResponse importHiredCandidate(@RequestParam("file") MultipartFile filePath,
                                             @RequestParam String token) throws IOException {
        return hiredCandidateService.dropHireCandidateInDataBase(filePath, token);
    }

    /**
     * Returns list of candidate names
     */
    @GetMapping("/allcandidates")
    public List getAllHiredCandidate() throws IOException {
        return hiredCandidateService.getAllHiredCandidates();
    }

    /**
     * Takes id of candidate and give his/her profile details
     *
     * @param id
     * @return HireCandidateModel
     */
    @GetMapping("/viewprofile")
    public Optional<HiredCandidateModel> viewCandidateProfile(@RequestParam int id) throws IOException {
        return hiredCandidateService.viewCandidateProfile(id);
    }

    @PutMapping("/updatestatus")
    public UserResponse update(@RequestParam String candidateResponse,
                               @RequestParam String emailId) {
        return hiredCandidateService.updateStatus(candidateResponse, emailId);
    }
}
