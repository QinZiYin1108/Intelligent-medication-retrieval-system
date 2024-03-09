import java.util.HashMap;

public class machine {
    String hpCode;
    String KpCode;
    HashMap<String, Integer> machineDrug;

    public HashMap<String, Integer> getMaxDrug() {
        return maxDrug;
    }

    public void setMaxDrug(HashMap<String, Integer> maxDrug) {
        this.maxDrug = maxDrug;
    }

    HashMap<String,Integer> maxDrug;

    public machine(String hpCode, String kpCode, HashMap<String, Integer> machineDrug,HashMap<String,Integer> maxDrug) {
        this.hpCode = hpCode;
        this.KpCode = kpCode;
        this.machineDrug = machineDrug;
        this.maxDrug = maxDrug;
    }

    public String getHpCode() {
        return hpCode;
    }

    public void setHpCode(String hpCode) {
        this.hpCode = hpCode;
    }

    public String getKpCode() {
        return KpCode;
    }

    public void setKpCode(String kpCode) {
        KpCode = kpCode;
    }

    public HashMap<String, Integer> getMachineDrug() {
        return machineDrug;
    }

    public void setMachineDrug(HashMap<String, Integer> machineDrug) {
        this.machineDrug = machineDrug;
    }

}
