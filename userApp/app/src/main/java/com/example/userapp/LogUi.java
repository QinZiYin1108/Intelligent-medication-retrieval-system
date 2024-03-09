package com.example.userapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import kotlinx.coroutines.channels.ActorKt;

public class LogUi extends AppCompatActivity {

    private Button over;
    private Button register;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logui);

        initLog();

        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userE = email.getText().toString();
                String userP = password.getText().toString();
                if (userE.isEmpty() || userP.isEmpty()){
                    AlertDialog.Builder builderEmpty = new AlertDialog.Builder(LogUi.this);
                    builderEmpty.setMessage("账户邮箱和密码不能为空");
                    builderEmpty.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builderEmpty.create();
                    alertDialog.show();
                }else {
                    int key = 0;
                    for (Map.Entry<String,user> entry : AllData.userData.entrySet()){
                        if (entry.getValue().getUserEmail().equals(userE)){
                            key++;
                            if (entry.getValue().getUserPassword().equals(userP)){
                                openThread1(entry.getKey());
                            }else {
                                AlertDialog.Builder builderEmpty = new AlertDialog.Builder(LogUi.this);
                                builderEmpty.setMessage("密码不正确");
                                builderEmpty.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = builderEmpty.create();
                                alertDialog.show();
                            }
                        }
                    }

                    if (key==0){
                        AlertDialog.Builder builderEmpty = new AlertDialog.Builder(LogUi.this);
                        builderEmpty.setMessage("账户未注册");
                        builderEmpty.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog = builderEmpty.create();
                        alertDialog.show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogUi.this, Register.class);
                startActivity(intent);

                LogUi.this.finish();
            }
        });
    }

    private void openThread1(String userCode) {
        AllData.trueUserCode = userCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AllData.readRecord(userCode);
                    AllData.readHospital();
                    AllData.readDocuter();
                    AllData.readMachine();
                    AllData.readKeeper();

                    Intent intent = new Intent(LogUi.this, overall_main.class);
                    startActivity(intent);
                    LogUi.this.finish();

                    AllData.logKey = false;
                    AllData.userKey = true;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (AllData.userKey){
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                String sqlDrug = "SELECT COUNT(*) FROM drug";
                                try {
                                    PreparedStatement preDrug = AllData.connection.prepareStatement(sqlDrug);

                                    ResultSet reDrug = preDrug.executeQuery();
                                    reDrug.next();
                                    int drugCount = reDrug.getInt(1);
                                    reDrug.close();
                                    preDrug.close();

                                    if (drugCount!=AllData.drugCode.size()){
                                        AllData.readDrug();
                                    }

//                                    AllData.readMachine();
//                                    AllData.readKeeper();

                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }).start();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void initLog() {
        over = findViewById(R.id.overButton);
        register = findViewById(R.id.registerButton);
        email = findViewById(R.id.getEmail);
        password = findViewById(R.id.getPassword);
    }
}
