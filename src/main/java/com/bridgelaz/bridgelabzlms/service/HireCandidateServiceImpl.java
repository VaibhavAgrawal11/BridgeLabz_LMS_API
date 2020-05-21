package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.HiredCandidateDTO;
import com.bridgelaz.bridgelabzlms.dto.UserResponse;
import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import com.bridgelaz.bridgelabzlms.repository.HiredCandidateRepository;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class HireCandidateServiceImpl implements IHireCandidateService {

    @Autowired
    private HiredCandidateRepository hiredCandidateRepository;

    @Autowired
    private ModelMapper modelMapper;

    HiredCandidateDTO hiredCandidate = new HiredCandidateDTO();

    /**
     * Prepare list from excel file
     *
     * @param filePath
     * @return list
     */
    public List<List<XSSFCell>> getHiredCandidate(String filePath) {
        List<List<XSSFCell>> sheetData = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath)) {
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
    public void saveCandidateDetails(List<List<XSSFCell>> sheetData) {
        XSSFCell cell;
        for (List<XSSFCell> list : sheetData) {
            int j = 0;
            cell = list.get(j++);
            hiredCandidate.setId((int) cell.getNumericCellValue());
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
            hiredCandidate.setMobileNumber(String.valueOf(cell.getNumericCellValue()));
            cell = list.get(j++);
            hiredCandidate.setPermanentPincode(String.valueOf(cell.getNumericCellValue()));
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
            cell = list.get(j++);
            hiredCandidate.setStatus(cell.getStringCellValue());
            cell = list.get(j++);
            hiredCandidate.setCreatorStamp(cell.getDateCellValue());
            cell = list.get(j++);
            hiredCandidate.setCreatorUser(cell.getStringCellValue());
            HiredCandidateModel hiredCandidateModel = modelMapper.map(hiredCandidate, HiredCandidateModel.class);
            hiredCandidateRepository.save(hiredCandidateModel);
        }
    }

    /**
     * Display all the hired candidate list
     *
     * @return candidateList
     */
    public List<String> getAllHiredCandidates() {
        List<HiredCandidateModel> list = hiredCandidateRepository.findAll();
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
    public UserResponse dropHireCandidateInDataBase(String filePath) {
        List<List<XSSFCell>> hiredCandidate = getHiredCandidate(filePath);
        saveCandidateDetails(hiredCandidate);
        return new UserResponse(200, "Successfully Noted");
    }

    /**
     * Take id of candidate and display profile
     *
     * @param id
     * @return hiredCandidateModel
     */
    @Override
    public Optional<HiredCandidateModel> viewCandidateProfile(Integer id) {
        return hiredCandidateRepository.findById(id);
    }
}
