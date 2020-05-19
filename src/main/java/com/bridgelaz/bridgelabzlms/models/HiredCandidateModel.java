package com.bridgelaz.bridgelabzlms.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity(name = "Hired_Candidate")
public class HiredCandidateModel {
    @Id
    private int Id;
    private String First_Name;
    private String Middle_Name;
    private String Last_Name;
    private String eamilId;
    private String Hired_City;
    private String Degree;
    private Date Hired_Date;
    private String Mobile_Number;
    private String Permanent_Pincode;
    private String Hired_Lab;
    private String Attitude;
    private String Communication_Remark;
    private String Knowledge_Remark;
    private String Aggregate_Remark;
    private String Status;
    private Date Creator_Stamp;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getMiddle_Name() {
        return Middle_Name;
    }

    public void setMiddle_Name(String middle_Name) {
        Middle_Name = middle_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getEamilId() {
        return eamilId;
    }

    public void setEamilId(String eamilId) {
        this.eamilId = eamilId;
    }

    public String getHired_City() {
        return Hired_City;
    }

    public void setHired_City(String hired_City) {
        Hired_City = hired_City;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public Date getHired_Date() {
        return Hired_Date;
    }

    public void setHired_Date(Date hired_Date) {
        Hired_Date = hired_Date;
    }

    public String getMobile_Number() {
        return Mobile_Number;
    }

    public void setMobile_Number(String mobile_Number) {
        Mobile_Number = mobile_Number;
    }

    public String getPermanent_Pincode() {
        return Permanent_Pincode;
    }

    public void setPermanent_Pincode(String permanent_Pincode) {
        Permanent_Pincode = permanent_Pincode;
    }

    public String getHired_Lab() {
        return Hired_Lab;
    }

    public void setHired_Lab(String hired_Lab) {
        Hired_Lab = hired_Lab;
    }

    public String getAttitude() {
        return Attitude;
    }

    public void setAttitude(String attitude) {
        Attitude = attitude;
    }

    public String getCommunication_Remark() {
        return Communication_Remark;
    }

    public void setCommunication_Remark(String communication_Remark) {
        Communication_Remark = communication_Remark;
    }

    public String getKnowledge_Remark() {
        return Knowledge_Remark;
    }

    public void setKnowledge_Remark(String knowledge_Remark) {
        Knowledge_Remark = knowledge_Remark;
    }

    public String getAggregate_Remark() {
        return Aggregate_Remark;
    }

    public void setAggregate_Remark(String aggregate_Remark) {
        Aggregate_Remark = aggregate_Remark;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Date getCreator_Stamp() {
        return Creator_Stamp;
    }

    public void setCreator_Stamp(Date creator_Stamp) {
        Creator_Stamp = creator_Stamp;
    }

    public String getCreator_User() {
        return Creator_User;
    }

    public void setCreator_User(String creator_User) {
        Creator_User = creator_User;
    }

    private String Creator_User;
}
