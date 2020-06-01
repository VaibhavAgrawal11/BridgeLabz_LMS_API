package com.bridgelaz.bridgelabzlms.contoller;

import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.service.IHireCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

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
     * @param token
     * @return UserResponse
     * @throws MessagingException
     * @throws CustomServiceException
     */
    @PostMapping("/takecandidatelist")
    public ResponseEntity<UserResponse> importHiredCandidate(@RequestParam("file") MultipartFile filePath,
                                                             @RequestParam String token) throws MessagingException, CustomServiceException {
        return new ResponseEntity<>(hiredCandidateService.dropHireCandidateInDataBase(filePath, token)
                , HttpStatus.CREATED);
    }

    /**
     * Returns list of candidate names
     *
     * @return
     * @throws CustomServiceException
     */
    @GetMapping("/allcandidates")
    public ResponseEntity<List> getAllHiredCandidate() throws CustomServiceException {
        return new ResponseEntity<>(hiredCandidateService.getAllHiredCandidates()
                , HttpStatus.MULTI_STATUS);
    }

    /**
     * Takes id of candidate and give his/her profile details
     *
     * @param id
     * @return HireCandidateModel
     */
    @GetMapping("/viewprofile")
    public ResponseEntity<UserResponse> viewCandidateProfile(@RequestParam int id) throws CustomServiceException {
        return new ResponseEntity<>(hiredCandidateService.viewCandidateProfile(id), HttpStatus.OK);
    }

    /**
     * Update status of candidate table
     *
     * @param candidateResponse
     * @param emailId
     * @return UserResponse
     * @throws CustomServiceException
     */
    @GetMapping("/updatestatus")
    public ResponseEntity<UserResponse> update(@RequestParam String candidateResponse,
                                               @RequestParam String emailId) throws CustomServiceException {
        return new ResponseEntity<>(hiredCandidateService.updateStatus(candidateResponse, emailId)
                , HttpStatus.OK);
    }
}
