package com.example.userapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class recodUI extends AppCompatActivity {

    private RecyclerView viewRecycle;
    private ArrayList<recordDrug> recordDrugs;
    private ImageView imageBack;
    private TextView docuterName;
    private TextView docuterCode;
    private TextView hospitalName;
    private TextView recordPrice;
    private TextView recordTime;
    private TextView recordDrug;
    private Button fukuanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_ui);

        initRecord();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(recodUI.this, userUI.class);
                startActivity(intent);
                recodUI.this.finish();
            }
        });

        recordDrug.setText("药品编号"+AllData.trueRecordCoded);
        docuterName.setText("主治医生："+AllData.docuterData.get(AllData.recordData.get(AllData.trueRecordCoded).getDcCode()).getDcName());
        docuterCode.setText("医生编号："+AllData.recordData.get(AllData.trueRecordCoded).getDcCode());
        String hpCode = AllData.docuterData.get(AllData.recordData.get(AllData.trueRecordCoded).getDcCode()).getHpCode();
        String hospitalNameSt = AllData.hospitalData.get(hpCode).getHp_name();
        hospitalName.setText("所属医院："+hospitalNameSt);
        Double price = 0.0;

        for (recordDrug re : AllData.recordData.get(AllData.trueRecordCoded).getAllDrug()){
            price += re.getRdNumber()*AllData.drugData.get(re.getDgCode()).getDgPrive();
        }

        recordPrice.setText("需付总价："+String.valueOf(price));
        Timestamp reTime = AllData.recordData.get(AllData.trueRecordCoded).getReTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用 SimpleDateFormat 格式化 Timestamp 对象为字符串
        String formattedString = dateFormat.format(reTime);
        recordTime.setText("下单时间"+formattedString);

        TestAdapter2 testAdapter2 = new TestAdapter2(AllData.recordData.get(AllData.trueRecordCoded).getAllDrug());
        viewRecycle.setAdapter(testAdapter2);

        fukuanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(recodUI.this);
                builder.setTitle("提示");
                builder.setMessage("您确定要支付吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击确定按钮后的处理
                       Thread thread3 = new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String sqlRe  = "UPDATE record SET re_type = '已付款' where re_code = ?";
                               String sqlRd = "UPDATE record_drug SET rd_type = '已付款' where re_code = ?";

                               try {
                                   PreparedStatement pre1 = AllData.connection.prepareStatement(sqlRe);
                                   pre1.setString(1,AllData.trueRecordCoded);
                                   pre1.executeUpdate();
                                   pre1.close();

                                   PreparedStatement pre2 = AllData.connection.prepareStatement(sqlRd);
                                   pre2.setString(1,AllData.trueRecordCoded);
                                   pre2.executeUpdate();
                                   pre2.close();

                               } catch (SQLException e) {
                                   throw new RuntimeException(e);
                               }
                           }
                       });

                       thread3.start();
                        try {
                            thread3.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        AllData.recordData.get(AllData.trueRecordCoded).setReType("已付款");

                        for (com.example.userapp.recordDrug rd : AllData.recordData.get(AllData.trueRecordCoded).getAllDrug()){
                            rd.setRdType("已付款");
                        }

                        dialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(recodUI.this);
                        builder.setTitle("提示");
                        builder.setMessage("支付成功");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击确定按钮后的处理
                                dialog.dismiss();
                                Intent intent = new Intent(recodUI.this, userUI.class);
                                startActivity(intent);
                                recodUI.this.finish();
                            }
                        });
                        builder.create().show();
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

    private void initRecord() {
        recordDrug = findViewById(R.id.record_ui_recordCode);
        viewRecycle = findViewById(R.id.rv_record);
        imageBack = findViewById(R.id.imageBack_two);
        docuterName = findViewById(R.id.record_ui_docuterName);
        docuterCode = findViewById(R.id.record_ui_docuterCode);
        hospitalName = findViewById(R.id.record_ui_hospitalName);
        recordPrice = findViewById(R.id.record_ui_recordPrice);
        recordTime = findViewById(R.id.record_ui_recordTime);
        fukuanButton = findViewById(R.id.fukuanButton);
    }
}
