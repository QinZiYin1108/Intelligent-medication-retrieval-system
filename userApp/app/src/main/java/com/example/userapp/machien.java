package com.example.userapp;

import java.util.ArrayList;

public class machien {
    String kpCode;
    String hpCode;
    ArrayList<machine_drug> AllmdDrug;

    public machien(String kpCode, String hpCode, ArrayList<machine_drug> allmdDrug) {
        this.kpCode = kpCode;
        this.hpCode = hpCode;
        AllmdDrug = allmdDrug;
    }

    public String getKpCode() {
        return kpCode;
    }

    public void setKpCode(String kpCode) {
        this.kpCode = kpCode;
    }

    public String getHpCode() {
        return hpCode;
    }

    public void setHpCode(String hpCode) {
        this.hpCode = hpCode;
    }

    public ArrayList<machine_drug> getAllmdDrug() {
        return AllmdDrug;
    }

    public void setAllmdDrug(ArrayList<machine_drug> allmdDrug) {
        AllmdDrug = allmdDrug;
    }
}
