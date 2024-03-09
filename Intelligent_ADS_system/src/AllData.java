import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class AllData {
    static boolean keeperKey = false;
    static boolean logKey = true;
    static boolean hospitalKey = false;
    static boolean docuterKey = false;
    static String hpStyle = "First";
    static HashMap<String,record> recordData = new HashMap<>();
    static ArrayList<String> recordCode = new ArrayList<>();
    static HashMap<String,recordDrug> recordDrugData = new HashMap<>();
    static ArrayList<String> recordDrugCode = new ArrayList<>();
    static HashMap<String,manage> manageData = new HashMap<>();
    static ArrayList<String> manageCode = new ArrayList<>();
    static HashMap<String,machine> machineData = new HashMap<>();
    static ArrayList<String> machineCode = new ArrayList<>();
    static HashMap<String,drug> drugData = new HashMap<>();
    static ArrayList<String> drugCode = new ArrayList<>();
    static HashMap<String,docuter> docuterWaitingData = new HashMap<>();
    static ArrayList<String> docuterWaitingCode = new ArrayList<>();
    static HashMap<String,keeper> keeperWaitingData = new HashMap<>();
    static ArrayList<String> keeperWaitingCode = new ArrayList<>();
    static HashMap<String,request> requestData = new HashMap<>();
    static ArrayList<String> requestCode = new ArrayList<>();
    static HashMap<String,hospital> hospitalData = new HashMap<>();
    static ArrayList<String> hospitalCode = new ArrayList<>();
    static HashMap<String,docuter> docuterData = new HashMap<>();
    static ArrayList<String> docuterCode = new ArrayList<>();
    static HashMap<String,keeper> keeperData = new HashMap<>();
    static ArrayList<String> keeperCode = new ArrayList<>();
    static HashMap<String,user> userData = new HashMap<>();
    static ArrayList<String> userCode = new ArrayList<>();
    static Connection connection;
    static Statement statement;


    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://140.143.140.88:3306/iamd?useUnicode=true&characterEncoding=utf8","yin","@YSCysc031108/");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AllData() throws SQLException {
        readAll();
    }

    public static void readAll() throws SQLException {
        readDocuter();
        readUser();
        readKeeper();
        readHospital();
        readRequest();
        readDocuterWaiting();
        readKeeperWaiting();
        readDrug();
    }

    public static void readRecordDrug(String dcCode) throws SQLException {
        recordDrugData.clear();
        recordDrugCode.clear();

        String sql = "SELECT * FROM record_drug where re_code IN (SELECT re_code FROM record where dc_code = ?) ORDER BY CAST(SUBSTRING(rd_code, 7) AS SIGNED)";
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setString(1,dcCode);
        ResultSet resultSet = prepared.executeQuery();
        while (resultSet.next()){
            String rdCode = resultSet.getString(1);
            String reCode = resultSet.getString(2);
            String dgCode = resultSet.getString(3);
            int rdNumber = resultSet.getInt(4);
            String rdType = resultSet.getString(5);

            recordDrug recordDrug = new recordDrug(reCode, dgCode, rdNumber,rdType);
            recordDrugData.put(rdCode,recordDrug);
            recordDrugCode.add(rdCode);
        }
        resultSet.close();
        prepared.close();
    }

    public static void readRecord(String dcCode) throws SQLException {
        recordData.clear();
        recordCode.clear();

        String sql = "SELECT * FROM record where dc_code = ? ORDER BY CAST(SUBSTRING(re_code, 7) AS SIGNED)";
        PreparedStatement pre = connection.prepareStatement(sql);
        pre.setString(1,dcCode);
        ResultSet resultSet = pre.executeQuery();
        while (resultSet.next()){
            String reCode = resultSet.getString(1);
            String reDcCode = resultSet.getString(2);
            String reUserCode = resultSet.getString(3);
            Timestamp reTime = resultSet.getTimestamp(4);

            record record = new record(reDcCode, reUserCode, reTime);
            recordData.put(reCode,record);
            recordCode.add(reCode);
        }
        resultSet.close();
        pre.close();
    }


    public static void readDrug() throws SQLException {
        drugCode.clear();
        drugData.clear();

        String sql = "SELECT * FROM drug ORDER BY CAST(SUBSTRING(dg_code, 4) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String dgCode = resultSet.getString(1);
            String dgName = resultSet.getString(2);
            String dgType = resultSet.getString(3);
            int dgPrice = resultSet.getInt(4);

            drug drug = new drug(dgName, dgType, dgPrice);
            drugData.put(dgCode,drug);
            drugCode.add(dgCode);
        }
        resultSet.close();
    }
    public static void readManage(String hpCode) throws SQLException {
        manageData.clear();
        manageCode.clear();

        String sql = "SELECT *,dg_code,mh_code FROM manage,machine_drug WHERE manage.md_code = machine_drug.md_code AND kp_code IN (SELECT kp_code FROM keeper WHERE hp_code = '"+hpCode+"')";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            String mgCode = resultSet.getString(1);
            String kpCode = resultSet.getString(2);
            Timestamp mgTime = resultSet.getTimestamp(4);
            int mgNumber = resultSet.getInt(5);
            String dgCode = resultSet.getString(6);
            String mhCode = resultSet.getString(7);

            LocalDateTime mgTimeLocalDateTime = mgTime.toLocalDateTime();
            HashMap<String, Integer> drugChange = new HashMap<>();
            drugChange.put(dgCode,mgNumber);

            manage manage = new manage(kpCode, mgTimeLocalDateTime, mhCode, drugChange);
            manageData.put(mgCode,manage);
            manageCode.add(mgCode);
        }
        resultSet.close();
    }

    public static void readMachine(String hpCode) throws SQLException {
        machineData.clear();
        machineCode.clear();

        String sql = "SELECT * FROM machine WHERE hp_code = ? ORDER BY CAST(SUBSTRING(mh_code, 4) AS SIGNED)";
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setString(1,hpCode);
        ResultSet resultSetMh = prepared.executeQuery();
        while (resultSetMh.next()){
            String mhCode = resultSetMh.getString(1);
            String kpCode = resultSetMh.getString(2);
            String sql2 = "SELECT * FROM machine_drug WHERE mh_code = '"+mhCode+"'";
            ResultSet resultSet1 = statement.executeQuery(sql2);
            HashMap<String, Integer> machineDrug = new HashMap<>();
            HashMap<String,Integer> maxDrug = new HashMap<>();
            while (resultSet1.next()){
                String dgCode = resultSet1.getString(2);
                int mdNumber = resultSet1.getInt(4);
                int maxNumber = resultSet1.getInt(5);
                machineDrug.put(dgCode,mdNumber);
                maxDrug.put(dgCode,maxNumber);
            }
            resultSet1.close();

            machine machine = new machine(hpCode, kpCode, machineDrug,maxDrug);
            machineData.put(mhCode,machine);
            machineCode.add(mhCode);
        }
        resultSetMh.close();
        prepared.close();
    }

    public static void readKeeperWaiting() throws SQLException {
        keeperWaitingData.clear();
        keeperWaitingCode.clear();

        String sql = "SELECT * FROM keeper_waiting ORDER BY CAST(SUBSTRING(kw_code, 4) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String kwCode = resultSet.getString(1);
            String hpCode = resultSet.getString(2);
            String kwPassword = resultSet.getString(3);
            String kwEmail = resultSet.getString(4);
            String kwName = resultSet.getString(5);
            String kwIDcard = resultSet.getString(6);
            int kwAge = resultSet.getInt(7);
            String kwSex = resultSet.getString(8);

            keeper keeper = new keeper(hpCode, kwPassword, kwEmail, kwName, kwIDcard,kwAge, kwSex);
            keeperWaitingData.put(kwCode,keeper);
            keeperWaitingCode.add(kwCode);
        }
        resultSet.close();
    }

    public static void readDocuterWaiting() throws SQLException {
        docuterWaitingData.clear();
        docuterWaitingCode.clear();

        String sql = "SELECT * FROM docuter_waiting ORDER BY CAST(SUBSTRING(dw_code, 4) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String dwCode = resultSet.getString(1);
            String hpCode = resultSet.getString(2);
            String dwEmail = resultSet.getString(3);
            String dwPassword = resultSet.getString(4);
            String dwName = resultSet.getString(5);
            String dwID = resultSet.getString(6);
            String dwIDcard = resultSet.getString(7);
            int dwAge = resultSet.getInt(8);
            String dwSex = resultSet.getString(9);
            String dwPhone = resultSet.getString(10);

            docuter docuter = new docuter(hpCode, dwEmail, dwPassword, dwName, dwID, dwIDcard, dwAge, dwSex, dwPhone);
            docuterWaitingData.put(dwCode,docuter);
            docuterWaitingCode.add(dwCode);
        }
        resultSet.close();
    }

    public static void readRequest() throws SQLException {
        requestCode.clear();
        requestData.clear();

        String sql = "SELECT  * FROM request_employment ORDER BY CAST(SUBSTRING(re_code, 5) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String reCode = resultSet.getString(1);
            String emCode = resultSet.getString(2);
            String hpCode = resultSet.getString(3);
            Timestamp reTimeOn = resultSet.getTimestamp(4);
            Timestamp reTimeOk = resultSet.getTimestamp(5);
            String reState = resultSet.getString(6);

            LocalDateTime reTimeOnLocalDateTime = reTimeOn.toLocalDateTime();
            LocalDateTime reTimeOkLocalDateTime;
            if (reTimeOk.equals(null)){
                reTimeOkLocalDateTime = null;
            }else{
                reTimeOkLocalDateTime = reTimeOk.toLocalDateTime();
            }

            request request = new request(emCode, hpCode, reState, reTimeOnLocalDateTime, reTimeOkLocalDateTime);
            requestData.put(reCode,request);
            requestCode.add(reCode);
        }
        resultSet.close();
    }

    public static void readUser() throws SQLException {
        userData.clear();
        userCode.clear();

        String sql = "SELECT * FROM users ORDER BY CAST(SUBSTRING(user_code, 3) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String usCode = resultSet.getString(1);
            String userEmail = resultSet.getString(2);
            String userPassword = resultSet.getString(3);
            String userName = resultSet.getString(4);
            int userAge = resultSet.getInt(5);
            String userSex = resultSet.getString(6);
            String userPhone = resultSet.getString(7);

            user user = new user(userEmail, userPassword, userName, userAge, userSex, userPhone);
            userData.put(usCode,user);
            userCode.add(usCode);
        }
        resultSet.close();
    }

    public static void readKeeper() throws SQLException {
        keeperData.clear();
        keeperCode.clear();

        String sql = "SELECT * FROM keeper ORDER BY CAST(SUBSTRING(kp_code, 3) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()){
            String kpCode = resultSet.getString(1);
            String hpCode = resultSet.getString(2);
            String kpPassword = resultSet.getString(3);
            String kpEmail = resultSet.getString(4);
            String kpName = resultSet.getString(5);
            String kpIDcard = resultSet.getString(6);
            int kpAge = resultSet.getInt(7);
            String kpSex = resultSet.getString(8);

            keeper keeper = new keeper(hpCode, kpPassword, kpEmail, kpName, kpIDcard,kpAge, kpSex);
            keeperData.put(kpCode,keeper);
            keeperCode.add(kpCode);
        }

        resultSet.close();
    }

    public static void readDocuter() throws SQLException {
        docuterCode.clear();
        docuterData.clear();

        String sql = "SELECT * FROM docuter ORDER BY CAST(SUBSTRING(dc_code ,3) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()){
            String dcCode = resultSet.getString(1);
            String hpCode = resultSet.getString(2);
            String dcEmail = resultSet.getString(3);
            String dcPassword = resultSet.getString(4);
            String dcName = resultSet.getString(5);
            String dcID = resultSet.getString(6);
            String dcIDcard = resultSet.getString(7);
            int dcAge = resultSet.getInt(8);
            String dcSex = resultSet.getString(9);
            String dcPhone = resultSet.getString(10);

            docuter docuter = new docuter(hpCode, dcEmail, dcPassword, dcName, dcID, dcIDcard, dcAge, dcSex, dcPhone);
            docuterData.put(dcCode,docuter);
            docuterCode.add(dcCode);
        }
        resultSet.close();
    }

    public static void readHospital() throws SQLException {
        hospitalData.clear();
        hospitalCode.clear();

        String sql = "SELECT * FROM hospital ORDER BY CAST(SUBSTRING(hp_code, 3) AS SIGNED)";
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()){
            String hpCode = resultSet.getString(1);
            String hpEmail = resultSet.getString(2);
            String hpPassword = resultSet.getString(3);
            String hpName = resultSet.getString(4);
            String hpLicense = resultSet.getString(5);
            String hpBusLi = resultSet.getString(6);
            String hpLegalPerson = resultSet.getString(7);
            String hpIDLegal = resultSet.getString(8);

            hospital hospital = new hospital(hpEmail, hpPassword, hpName, hpLicense, hpBusLi, hpLegalPerson, hpIDLegal);
            hospitalCode.add(hpCode);
            hospitalData.put(hpCode,hospital);
        }
        resultSet.close();
    }
}
