package com.example.userapp;

public class machine_drug {
    String mdCode;
    String dgCode;
    int mdNumber;

    public machine_drug(String mdCode,String dgCode, int mdNumber) {
        this.dgCode = dgCode;
        this.mdCode = mdCode;
        this.mdNumber = mdNumber;
    }

    public String getMdCode() {
        return mdCode;
    }

    public void setMdCode(String mdCode) {
        this.mdCode = mdCode;
    }

    public String getDgCode() {
        return dgCode;
    }

    public void setDgCode(String dgCode) {
        this.dgCode = dgCode;
    }

    public int getMdNumber() {
        return mdNumber;
    }

    public void setMdNumber(int mdNumber) {
        this.mdNumber = mdNumber;
    }
}
