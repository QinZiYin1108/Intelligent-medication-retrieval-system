package com.example.userapp;

import java.sql.Timestamp;
import java.util.ArrayList;

public class record {
    String dcCode;
    String userCode;
    Timestamp reTime;
    String reType;
    ArrayList<recordDrug> allDrug;

    public record(String dcCode, String userCode, Timestamp reTime, ArrayList<recordDrug> allDrug,String reType) {
        this.dcCode = dcCode;
        this.userCode = userCode;
        this.reTime = reTime;
        this.allDrug = allDrug;
        this.reType = reType;
    }

    public String getReType() {
        return reType;
    }

    public void setReType(String reType) {
        this.reType = reType;
    }

    public String getDcCode() {
        return dcCode;
    }

    public void setDcCode(String dcCode) {
        this.dcCode = dcCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Timestamp getReTime() {
        return reTime;
    }

    public void setReTime(Timestamp reTime) {
        this.reTime = reTime;
    }


    public ArrayList<recordDrug> getAllDrug() {
        return allDrug;
    }

    public void setAllDrug(ArrayList<recordDrug> allDrug) {
        this.allDrug = allDrug;
    }
}
