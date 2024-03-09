import java.sql.Timestamp;

public class record {
    String dcCode;
    String userCode;
    Timestamp reTime;

    String rrType;

    public record(String dcCode, String userCode, Timestamp reTime) {
        this.dcCode = dcCode;
        this.userCode = userCode;
        this.reTime = reTime;
        this.rrType = "未支付";
    }

    public String getRrType() {
        return rrType;
    }

    public void setRrType(String rrType) {
        this.rrType = rrType;
    }

    public String getDcCode() {
        return dcCode;
    }

    public void setDcCode(String dcCode) {
        this.dcCode = dcCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Timestamp getReTime() {
        return reTime;
    }

    public void setReTime(Timestamp reTime) {
        this.reTime = reTime;
    }
}
