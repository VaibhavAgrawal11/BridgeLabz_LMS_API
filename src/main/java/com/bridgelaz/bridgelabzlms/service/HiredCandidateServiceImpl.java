package com.bridgelaz.bridgelabzlms.service;

import com.bridgelaz.bridgelabzlms.dto.HiredCandidateDTO;
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

@Service
public class HiredCandidateServiceImpl implements HireCandidateService {

    @Autowired
    private HiredCandidateRepository hiredCandidateRepository;

    @Autowired
    private ModelMapper modelMapper;

    HiredCandidateDTO hiredCandidate = new HiredCandidateDTO();

    @Override
    public List getHiredCandidate(String filePath) {
        List sheetData = new ArrayList();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                XSSFRow row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                List data = new ArrayList();
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

    @Override
    public void saveCandidateDetails(List sheetData) {
        XSSFCell cell;
        for (int i = 1; i < sheetData.size(); i++) {
            int j = 0;
            List list = (List) sheetData.get(i);
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setId((int) cell.getNumericCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setFirst_Name(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setMiddle_Name(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setLast_Name(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setEamilId(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setHired_City(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setDegree(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setHired_Date(cell.getDateCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setMobile_Number(String.valueOf(cell.getNumericCellValue()));
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setPermanent_Pincode(String.valueOf(cell.getNumericCellValue()));
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setHired_Lab(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setAttitude(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setCommunication_Remark(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setKnowledge_Remark(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setAggregate_Remark(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setStatus(cell.getStringCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setCreator_Stamp(cell.getDateCellValue());
            cell = (XSSFCell) list.get(j++);
            hiredCandidate.setCreator_User(cell.getStringCellValue());
            HiredCandidateModel hiredCandidateModel = modelMapper.map(hiredCandidate, HiredCandidateModel.class);
            hiredCandidateRepository.save(hiredCandidateModel);
        }
    }
}
