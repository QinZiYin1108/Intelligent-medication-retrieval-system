package com.example.userapp;

public class keeper {
    String hpCode;
    String kpPassword;
    String kpEmail;
    String kpName;
    String kpIDcard;
    int kpAge;
    String kpSex;

    public keeper(String hpCode, String kpPassword, String kpEmail, String kpName, String kpIDcard,int kpAge, String kpSex) {
        this.kpIDcard = kpIDcard;
        this.hpCode = hpCode;
        this.kpPassword = kpPassword;
        this.kpEmail = kpEmail;
        this.kpName = kpName;
        this.kpAge = kpAge;
        this.kpSex = kpSex;
    }

    public String getHpCode() {
        return hpCode;
    }

    public void setHpCode(String hpCode) {
        this.hpCode = hpCode;
    }

    public String getKpPassword() {
        return kpPassword;
    }

    public void setKpPassword(String kpPassword) {
        this.kpPassword = kpPassword;
    }

    public String getKpEmail() {
        return kpEmail;
    }

    public void setKpEmail(String kpEmail) {
        this.kpEmail = kpEmail;
    }

    public String getKpName() {
        return kpName;
    }

    public void setKpName(String kpName) {
        this.kpName = kpName;
    }

    public String getKpIDcard() {
        return kpIDcard;
    }

    public void setKpIDcard(String kpIDcard) {
        this.kpIDcard = kpIDcard;
    }

    public int getKpAge() {
        return kpAge;
    }

    public void setKpAge(int kpAge) {
        this.kpAge = kpAge;
    }

    public String getKpSex() {
        return kpSex;
    }

    public void setKpSex(String kpSex) {
        this.kpSex = kpSex;
    }
}
