package com.example.userapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class AllData {
    static String trueMachineCode = "";
    static String trueRecordCoded = "";
    static String trueUserCode = "";
    static boolean logKey = true;
    static boolean userKey = false;
    static Connection connection = null;
    static HashMap<String,keeper> keeperData = new HashMap<>();
    static ArrayList<String> keeperCode = new ArrayList<>();
    static HashMap<String,machien> machineData = new HashMap<>();
    static ArrayList<String> machineCode = new ArrayList<>();
    static HashMap<String,drug> drugData = new HashMap<>();
    static ArrayList<String> drugCode = new ArrayList<>();
    static HashMap<String,record> recordData = new HashMap<>();
    static ArrayList<String> recordCode = new ArrayList<>();
    static HashMap<String,user> userData = new HashMap<>();
    static ArrayList<String> userCodes = new ArrayList<>();
    static HashMap<String,docuter> docuterData = new HashMap<>();
    static ArrayList<String> docuterCode = new ArrayList<>();
    static HashMap<String,hospital> hospitalData = new HashMap<>();
    static ArrayList<String> hospitalCode = new ArrayList<>();

    public static boolean getConnection() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://140.143.140.88:3306/iamd?useUnicode=true&characterEncoding=UTF-8","yin","@YSCysc031108/");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void readKeeper() throws SQLException {
        keeperData.clear();
        keeperCode.clear();

        Statement statement = connection.createStatement();
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
        statement.close();
    }

    public static void readMachine() throws SQLException {
        machineData.clear();
        machineCode.clear();

        String sqlMh = "SELECT * FROM machine ORDER BY CAST(SUBSTRING(mh_code, 4) AS SIGNED)";
        PreparedStatement pre1 = connection.prepareStatement(sqlMh);
        ResultSet reMh = pre1.executeQuery();
        while (reMh.next()){
            String mhCode = reMh.getString(1);
            String kpCode = reMh.getString(2);
            String hpCode = reMh.getString(3);
            ArrayList<machine_drug> machineDrugs = new ArrayList<>();

            String sqlMd = "SELECT * FROM machine_drug WHERE mh_code = ? ";
            PreparedStatement pre2 = connection.prepareStatement(sqlMd);
            pre2.setString(1,mhCode);
            ResultSet reMd = pre2.executeQuery();
            while (reMd.next()){
                String mdCode = reMd.getString(1);
                String dgCode = reMd.getString(2);
                int mdNumber = reMd.getInt(4);
                machine_drug machineDrug = new machine_drug(mdCode,dgCode, mdNumber);
                machineDrugs.add(machineDrug);
            }
            reMd.close();
            pre2.close();

            machien machien = new machien(kpCode, hpCode, machineDrugs);
            machineData.put(mhCode,machien);
            machineCode.add(mhCode);
        }
        reMh.close();
        pre1.close();
    }

    public static void readHospital() throws SQLException {
        hospitalData.clear();
        hospitalCode.clear();

        Statement statement = connection.createStatement();
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
        statement.close();
    }

    public static void readDocuter() throws SQLException {
        docuterCode.clear();
        docuterData.clear();

        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM docuter";
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
        statement.close();
    }

    public static void readUser() throws SQLException {
        userData.clear();
        userCodes.clear();

        String sql = "SELECT * FROM users ORDER BY CAST(SUBSTRING(user_code, 3) AS SIGNED)";
        PreparedStatement pre = connection.prepareStatement(sql);
        ResultSet resultSet = pre.executeQuery();
        while (resultSet.next()){
            String userCode = resultSet.getString(1);
            String userEmail = resultSet.getString(2);
            String userPassword = resultSet.getString(3);
            String userName = resultSet.getString(4);
            int userAge = resultSet.getInt(5);
            String userSex = resultSet.getString(6);
            String userPhone = resultSet.getString(7);

            user user = new user(userEmail, userPassword, userName, userAge, userSex, userPhone);
            userData.put(userCode,user);
            userCodes.add(userCode);
        }
        resultSet.close();
        pre.close();
    }

    public static void readRecord(String userCode) throws SQLException {
        recordData.clear();
        recordCode.clear();

        String sql = "SELECT * FROM record where user_code = ? ORDER BY re_time";
        PreparedStatement pre = connection.prepareStatement(sql);
        pre.setString(1,userCode);
        ResultSet resultSet = pre.executeQuery();
        while (resultSet.next()){
            String reCode = resultSet.getString(1);
            String reDcCode = resultSet.getString(2);
            String reUserCode = resultSet.getString(3);
            Timestamp reTime = resultSet.getTimestamp(4);
            String reType = resultSet.getString(5);

            String sql2 = "SELECT * FROM record_drug where re_code = ?";
            PreparedStatement pre2 = AllData.connection.prepareStatement(sql2);
            pre2.setString(1,reCode);
            ResultSet resultSet1 = pre2.executeQuery();
            ArrayList<recordDrug> allDrug = new ArrayList<>();
            while (resultSet1.next()){
                String rdCode = resultSet1.getString(1);
                String dgCode = resultSet1.getString(3);
                int drugNumber = resultSet1.getInt(4);
                String type = resultSet1.getString(5);
                recordDrug recordDrug = new recordDrug(rdCode,dgCode, drugNumber, type);
                allDrug.add(recordDrug);
            }
            record recordt = new record(reDcCode, reUserCode, reTime,allDrug,reType);
            recordData.put(reCode,recordt);
            recordCode.add(reCode);
        }
        resultSet.close();
        pre.close();
    }


    public static void readDrug() throws SQLException {
        drugCode.clear();
        drugData.clear();

        String sql = "SELECT * FROM drug;";
        Statement statement = connection.createStatement();
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
        statement.close();
    }
}
