import java.time.LocalDateTime;

public class request {
    String emCode;
    String hpCode;
    String reState;
    LocalDateTime reTimeOn;
    LocalDateTime reTimeOk;

    public request(String emCode, String hpCode, String reState, LocalDateTime reTimeOn, LocalDateTime reTimeOk) {
        this.emCode = emCode;
        this.hpCode = hpCode;
        this.reState = reState;
        this.reTimeOn = reTimeOn;
        this.reTimeOk = reTimeOk;
    }

    public String getEmCode() {
        return emCode;
    }

    public void setEmCode(String emCode) {
        this.emCode = emCode;
    }

    public String getHpCode() {
        return hpCode;
    }

    public void setHpCode(String hpCode) {
        this.hpCode = hpCode;
    }

    public String getReState() {
        return reState;
    }

    public void setReState(String reState) {
        this.reState = reState;
    }

    public LocalDateTime getReTimeOn() {
        return reTimeOn;
    }

    public void setReTimeOn(LocalDateTime reTimeOn) {
        this.reTimeOn = reTimeOn;
    }

    public LocalDateTime getReTimeOk() {
        return reTimeOk;
    }

    public void setReTimeOk(LocalDateTime reTimeOk) {
        this.reTimeOk = reTimeOk;
    }
}
