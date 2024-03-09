import java.time.LocalDateTime;
import java.util.HashMap;

public class manage {
    String kpCode;
    LocalDateTime mgTime;
    String mhCode;
    HashMap<String,Integer> drugChange;

    public manage(String kpCode, LocalDateTime mgTime, String mhCode, HashMap<String, Integer> drugChange) {
        this.kpCode = kpCode;
        this.mgTime = mgTime;
        this.mhCode = mhCode;
        this.drugChange = drugChange;
    }

    public String getKpCode() {
        return kpCode;
    }

    public void setKpCode(String kpCode) {
        this.kpCode = kpCode;
    }

    public LocalDateTime getMgTime() {
        return mgTime;
    }

    public void setMgTime(LocalDateTime mgTime) {
        this.mgTime = mgTime;
    }

    public String getMhCode() {
        return mhCode;
    }

    public void setMhCode(String mhCode) {
        this.mhCode = mhCode;
    }

    public HashMap<String, Integer> getDrugChange() {
        return drugChange;
    }

    public void setDrugChange(HashMap<String, Integer> drugChange) {
        this.drugChange = drugChange;
    }
}
