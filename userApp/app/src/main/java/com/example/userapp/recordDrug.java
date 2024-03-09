package com.example.userapp;

public class recordDrug {
    String rdCode;
    String dgCode;
    int rdNumber;
    String rdType;

    public recordDrug(String rdCode,String dgCode, int rdNumber, String rdType) {
        this.dgCode = dgCode;
        this.rdNumber = rdNumber;
        this.rdType = rdType;
        this.rdCode = rdCode;
    }

    public String getRdCode() {
        return rdCode;
    }

    public void setRdCode(String rdCode) {
        this.rdCode = rdCode;
    }

    public String getDgCode() {
        return dgCode;
    }

    public void setDgCode(String dgCode) {
        this.dgCode = dgCode;
    }

    public int getRdNumber() {
        return rdNumber;
    }

    public void setRdNumber(int rdNumber) {
        this.rdNumber = rdNumber;
    }

    public String getRdType() {
        return rdType;
    }

    public void setRdType(String rdType) {
        this.rdType = rdType;
    }
}
