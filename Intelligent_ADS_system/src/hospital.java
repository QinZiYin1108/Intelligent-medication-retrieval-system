public class hospital {
    String hp_email;
    String hp_password;
    String hp_name;
    String hp_license;
    String hp_busLi;
    String hp_legal_person;
    String hp_ID_legal;

    public hospital(String hp_email, String hp_password, String hp_name, String hp_license, String hp_busLi, String hp_legal_person, String hp_ID_legal) {
        this.hp_email = hp_email;
        this.hp_password = hp_password;
        this.hp_name = hp_name;
        this.hp_license = hp_license;
        this.hp_busLi = hp_busLi;
        this.hp_legal_person = hp_legal_person;
        this.hp_ID_legal = hp_ID_legal;
    }

    public String getHp_email() {
        return hp_email;
    }

    public void setHp_email(String hp_email) {
        this.hp_email = hp_email;
    }

    public String getHp_password() {
        return hp_password;
    }

    public void setHp_password(String hp_password) {
        this.hp_password = hp_password;
    }

    public String getHp_name() {
        return hp_name;
    }

    public void setHp_name(String hp_name) {
        this.hp_name = hp_name;
    }

    public String getHp_license() {
        return hp_license;
    }

    public void setHp_license(String hp_license) {
        this.hp_license = hp_license;
    }

    public String getHp_busLi() {
        return hp_busLi;
    }

    public void setHp_busLi(String hp_busLi) {
        this.hp_busLi = hp_busLi;
    }

    public String getHp_legal_person() {
        return hp_legal_person;
    }

    public void setHp_legal_person(String hp_legal_person) {
        this.hp_legal_person = hp_legal_person;
    }

    public String getHp_ID_legal() {
        return hp_ID_legal;
    }

    public void setHp_ID_legal(String hp_ID_legal) {
        this.hp_ID_legal = hp_ID_legal;
    }
}
