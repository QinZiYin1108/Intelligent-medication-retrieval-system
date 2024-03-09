package com.example.userapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class getDrug_record_UI extends AppCompatActivity {
    public static Thread thread;
    public static ArrayList<recordDrug> recordDrugs;
    private ImageView backButton;
    private RecyclerView viewRecycle;
    private Button over;
    private TextView total;
    private TextView keeperName;
    private TextView machineType;
    private TextView hpName;
    private ArrayList<String> AlldrugCodes;
    private HashMap<String,machine_drug> drugs;
    private AlertDialog.Builder builder_TWO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getdrug_record_ui);
        initgetDrugRecordUI();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getDrug_record_UI.this, getDrugUI.class);
                startActivity(intent);
                getDrug_record_UI.this.finish();
            }
        });

        try {
            total.setText("药单编号："+AllData.trueRecordCoded);
            String kpCode = AllData.machineData.get(AllData.trueMachineCode).getKpCode();
            String hpCode = AllData.machineData.get(AllData.trueMachineCode).getHpCode();
            keeperName.setText("机器管理员："+AllData.keeperData.get(kpCode).getKpName());
            hpName.setText("所属医院："+AllData.hospitalData.get(hpCode).getHp_name());

        ArrayList<machine_drug> allmdDrug = AllData.machineData.get(AllData.trueMachineCode).getAllmdDrug();

        AlldrugCodes = new ArrayList<>();
        drugs = new HashMap<>();
        recordDrugs = new ArrayList<>();

        for (machine_drug md : allmdDrug){
            String dgCode = md.getDgCode();
            AlldrugCodes.add(dgCode);
            drugs.put(dgCode,md);
            }

            for (recordDrug rd : AllData.recordData.get(AllData.trueRecordCoded).getAllDrug()){
                if (AlldrugCodes.contains(rd.getDgCode()) && rd.getRdNumber()<= drugs.get(rd.getDgCode()).getMdNumber() && rd.getRdType().equals("已付款")){
                    recordDrugs.add(rd);
                }
            }
            TestAdapter2 testAdapter2 = new TestAdapter2(recordDrugs);
            viewRecycle.setAdapter(testAdapter2);
        }catch (NullPointerException e){
            Intent intent = new Intent(getDrug_record_UI.this, getDrugUI.class);
            startActivity(intent);
            getDrug_record_UI.this.finish();
            e.printStackTrace();
        }

        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getDrug_record_UI.this);
                builder.setTitle("提示");
                builder.setMessage("您确定要取药吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(getDrug_record_UI.this, DrugShipmentUI.class);
                        startActivity(intent);
                        getDrug_record_UI.this.finish();

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (recordDrug recordDrug : recordDrugs) {
                                    String dgCode = recordDrug.getDgCode();
                                    String rdCode = recordDrug.getRdCode();
                                    String mdCode = drugs.get(dgCode).getMdCode();
                                    String sqlRe = "UPDATE record_drug SET rd_type = '已取走' WHERE rd_code = ?";
                                    String sqlMd = "UPDATE machine_drug SET md_number = ? WHERE md_code = ?";
                                    int number = drugs.get(dgCode).getMdNumber() - recordDrug.getRdNumber();
                                    try {

                                        PreparedStatement pre1 = AllData.connection.prepareStatement(sqlRe);
                                        PreparedStatement pre2 = AllData.connection.prepareStatement(sqlMd);
                                        pre1.setString(1,rdCode);
                                        pre2.setInt(1,number);
                                        pre2.setString(2,mdCode);

                                        pre1.executeUpdate();
                                        pre2.executeUpdate();
                                        pre1.close();
                                        pre2.close();

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                String sqlchange = "UPDATE record SET re_type = '已取完'";
                                String sqlRd = "SELECT COUNT(*) FROM record_drug WHERE re_code = ? AND rd_type = '已取走'";
                                try {
                                    PreparedStatement pre3 = AllData.connection.prepareStatement(sqlRd);
                                    pre3.setString(1,AllData.trueRecordCoded);

                                    ResultSet resultSet = pre3.executeQuery();
                                    resultSet.next();
                                    int count = resultSet.getInt(1);

                                    if (count==AllData.recordData.get(AllData.trueRecordCoded).getAllDrug().size()){
                                        PreparedStatement pre4 = AllData.connection.prepareStatement(sqlchange);
                                        pre4.executeUpdate();
                                        pre4.close();
                                    }
                                    pre3.close();

                                    AllData.readMachine();
                                    AllData.readRecord(AllData.trueUserCode);

                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        thread.start();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击取消按钮后的处理
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
    }

    private void initgetDrugRecordUI() {
        backButton = findViewById(R.id.imageBack_four);
        viewRecycle  = findViewById(R.id.rv_record_getDrug_two);
        over = findViewById(R.id.overButton_two);
        keeperName = findViewById(R.id.getdrug_ui_kpName);
        machineType = findViewById(R.id.getdrug_ui_type);
        hpName = findViewById(R.id.getdrug_ui_hpName);
        total = findViewById(R.id.record_ui_recordCode_two);

    }
}
