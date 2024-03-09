public class recordDrug {
    String reCode;
    String dgCode;
    int rdNumber;
    String rdType;

    public recordDrug(String reCode, String dgCode, int rdNumber, String rdType) {
        this.reCode = reCode;
        this.dgCode = dgCode;
        this.rdNumber = rdNumber;
        this.rdType = rdType;
    }

    public recordDrug(String reCode, String dgCode, int rdNumber) {
        this.reCode = reCode;
        this.dgCode = dgCode;
        this.rdNumber = rdNumber;
        this.rdType = "未付款";
    }

    public String getReCode() {
        return reCode;
    }

    public void setReCode(String reCode) {
        this.reCode = reCode;
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
