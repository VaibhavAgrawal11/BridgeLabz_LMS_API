package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.configuration.ApplicationConfiguration;
import com.bridgelaz.bridgelabzlms.dto.HiredCandidateDTO;
import com.bridgelaz.bridgelabzlms.exception.CustomServiceException;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import com.bridgelaz.bridgelabzlms.models.User;
import com.bridgelaz.bridgelabzlms.repository.HiredCandidateRepository;
import com.bridgelaz.bridgelabzlms.repository.UserRepository;
import com.bridgelaz.bridgelabzlms.response.UserResponse;
import com.bridgelaz.bridgelabzlms.util.Token;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.bridgelaz.bridgelabzlms.exception.CustomServiceException.ExceptionType.*;

@Service
public class HireCandidateServiceImpl implements IHireCandidateService {

    HiredCandidateDTO hiredCandidate = new HiredCandidateDTO();
    @Autowired
    private HiredCandidateRepository hiredCandidateRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private Token jwtToken;
    @Autowired
    private UserRepository userRepository;

    /**
     * Prepare list from excel file
     *
     * @param filePath
     * @return list
     */
    public List<List<XSSFCell>> getHiredCandidate(MultipartFile filePath) {
        List<List<XSSFCell>> sheetData = new ArrayList<>();
        try (InputStream fis = filePath.getInputStream()) {

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                XSSFRow row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                List<XSSFCell> data = new ArrayList<>();
                while (cells.hasNext()) {
                    XSSFCell cell = (XSSFCell) cells.next();
                    data.add(cell);
                }
                sheetData.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheetData;
    }

    /**
     * Take the sheetData list and drop it in data base
     *
     * @param sheetData
     */
    public void saveCandidateDetails(List<List<XSSFCell>> sheetData, String token) throws MessagingException, CustomServiceException {
        XSSFCell cell;
        boolean flag = true;
        for (List<XSSFCell> list : sheetData) {
            if (!flag) {
                int j = 0;
                cell = list.get(j++);
                hiredCandidate.setFirstName(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setMiddleName(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setLastName(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setEmailId(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setHiredCity(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setDegree(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setHiredDate(cell.getDateCellValue());
                cell = list.get(j++);
                hiredCandidate.setMobileNumber((long) cell.getNumericCellValue());
                cell = list.get(j++);
                hiredCandidate.setPermanentPincode((int) cell.getNumericCellValue());
                cell = list.get(j++);
                hiredCandidate.setHiredLab(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setAttitude(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setCommunicationRemark(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setKnowledgeRemark(cell.getStringCellValue());
                cell = list.get(j++);
                hiredCandidate.setAggregateRemark(cell.getStringCellValue());
                hiredCandidate.setStatus("pending");
                hiredCandidate.setCreatorStamp(LocalDateTime.now());
                //Getting user with help of JWT token
                User user = userRepository.findByEmail(
                        jwtToken.getUsernameFromToken(token)).get();
                hiredCandidate.setCreatorUser(user.getCreatorUser());
                HiredCandidateModel hiredCandidateModel = modelMapper.map(hiredCandidate, HiredCandidateModel.class);
                if (hiredCandidateModel == null)
                    throw new CustomServiceException(DATA_NOT_FOUND, "Null Values found");
                hiredCandidateRepository.save(hiredCandidateModel);
                hiredCandidateRepository.save(hiredCandidateModel);
                this.sendSelectionMail(hiredCandidateModel.getEmailId());
            }
            flag = false;
        }
    }

    /**
     * @param emailId
     * @throws MessagingException
     */
    private void sendSelectionMail(String emailId) throws MessagingException, CustomServiceException {
        HiredCandidateModel hiredCandidateModel = hiredCandidateRepository.findByEmailId(emailId)
                .orElseThrow(
                        () -> new CustomServiceException(INVALID_EMAIL_ID, "No such id exist in data base.")
                );
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(emailId);
        helper.setText("Hello " + hiredCandidateModel.getFirstName() + " " + hiredCandidateModel.getLastName() + "," + "\n\n" +
                "Congratulations, you are been shortlisted for our fellowship plan." + "\n" +
                "You need to respond ACCEPTED to continue with fellowship plan" + "\n" +
                "http://localhost:8081/hirecandidate/updatestatus?candidateResponse=" + null + "&emailId=" + emailId);
        helper.setSubject("Fellowship Shortlist");
        sender.send(message);
    }

    /**
     * Display all the hired candidate list
     *
     * @return candidateList
     */
    public List<String> getAllHiredCandidates() throws CustomServiceException {
        List<HiredCandidateModel> list = hiredCandidateRepository.findAll();
        if (list == null)
            throw new CustomServiceException(DATA_NOT_FOUND, "Null Values found");
        List<String> candidateList = new ArrayList<>();
        for (HiredCandidateModel hiredCandidateModel : list) {
            candidateList.add((Integer) hiredCandidateModel.getId() + " " + "--->"
                    + " " + hiredCandidateModel.getFirstName()
                    + "." + hiredCandidateModel.getMiddleName()
                    + "." + hiredCandidateModel.getLastName());
        }
        return candidateList;
    }

    @Override
    public UserResponse dropHireCandidateInDataBase(MultipartFile filePath, String token) throws MessagingException, CustomServiceException {
        List<List<XSSFCell>> hiredCandidate = getHiredCandidate(filePath);
        saveCandidateDetails(hiredCandidate, token);
        return new UserResponse(filePath
                , ApplicationConfiguration.getMessageAccessor().getMessage("107"));
    }

    /**
     * Updates candidate status responded by them
     *
     * @param candidateResponse
     * @param emailId
     * @return User Response
     */
    @Override
    public UserResponse updateStatus(String candidateResponse, String emailId) throws CustomServiceException {
        HiredCandidateModel hiredCandidateModel = hiredCandidateRepository.findByEmailId(emailId)
                .orElseThrow(
                        () -> new CustomServiceException(INVALID_EMAIL_ID, "No such id exist in data base.")
                );
        if (candidateResponse.equals("Accepted") || candidateResponse.equals("Rejected")) {
            hiredCandidateModel.setStatus(candidateResponse);
            hiredCandidateRepository.save(hiredCandidateModel);
            return new UserResponse(hiredCandidateModel,
                    ApplicationConfiguration.getMessageAccessor().getMessage("108"));
        }
        return new UserResponse(hiredCandidateModel
                , ApplicationConfiguration.getMessageAccessor().getMessage("109"));
    }

    /**
     * Take id of candidate and display profile
     *
     * @param id
     * @return hiredCandidateModel
     */
    @Override
    public UserResponse viewCandidateProfile(Integer id) throws CustomServiceException {
        return new UserResponse(hiredCandidateRepository.findById(id)
                .orElseThrow(
                        () -> new CustomServiceException(INVALID_ID, "No such id found")
                )
                , ApplicationConfiguration.getMessageAccessor().getMessage("105"));
    }
}
