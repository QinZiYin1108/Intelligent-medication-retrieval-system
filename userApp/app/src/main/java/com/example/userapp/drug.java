package com.example.userapp;

public class drug {
    String dgName;
    String dgType;
    double dgPrive;

    public drug(String dgName, String dgType, double dgPrive) {
        this.dgName = dgName;
        this.dgType = dgType;
        this.dgPrive = dgPrive;
    }

    public String getDgName() {
        return dgName;
    }

    public void setDgName(String dgName) {
        this.dgName = dgName;
    }

    public String getDgType() {
        return dgType;
    }

    public void setDgType(String dgType) {
        this.dgType = dgType;
    }

    public double getDgPrive() {
        return dgPrive;
    }

    public void setDgPrive(double dgPrive) {
        this.dgPrive = dgPrive;
    }
}
