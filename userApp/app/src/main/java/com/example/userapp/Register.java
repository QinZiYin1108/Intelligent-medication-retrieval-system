package com.example.userapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class Register extends AppCompatActivity {
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0){
                AlertDialog.Builder builderEmpty = new AlertDialog.Builder(Register.this);
                builderEmpty.setMessage("注册成功");
                builderEmpty.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Register.this, LogUi.class);
                        startActivity(intent);
                        Register.this.finish();
                    }
                });

                AlertDialog alertDialog = builderEmpty.create();
                alertDialog.show();
            }
        }
    };
    private Button backButton;
    private Button button;
    private EditText getReEmail;
    private EditText getRePassword;
    private EditText getRePasswordCont;
    private EditText getReName;
    private EditText getReAge;
    private RadioButton getReMan;
    private RadioButton getReWoman;
    private EditText getRePhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initRe();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getReEmail.getText().toString();
                String password = getRePassword.getText().toString();
                String passwordCont = getRePasswordCont.getText().toString();
                String name = getReName.getText().toString();
                String age = getReAge.getText().toString();
                String sex = "";
                if (getReMan.isChecked()){
                    sex = "男";
                }else if (getReWoman.isChecked()){
                    sex = "女";
                }
                String phone = getRePhone.getText().toString();

                if (email.isEmpty() || passwordCont.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty() || sex.isEmpty() || phone.isEmpty()){
                    AlertDialog.Builder builderEmpty = new AlertDialog.Builder(Register.this);
                    builderEmpty.setMessage("各项不能为空");
                    builderEmpty.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builderEmpty.create();
                    alertDialog.show();
                }else {
                    if (isEmail(email)){
                        if (passwordCont.equals(password)){
                            if (isNumber(age)){
                                insertUser(email,password,Integer.parseInt(age),sex,phone,name);
                            }else {
                                AlertDialog.Builder builderEmpty = new AlertDialog.Builder(Register.this);
                                builderEmpty.setMessage("年龄只能由数字组成");
                                builderEmpty.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = builderEmpty.create();
                                alertDialog.show();
                            }
                        }else {
                            AlertDialog.Builder builderEmpty = new AlertDialog.Builder(Register.this);
                            builderEmpty.setMessage("确认密码不正确");
                            builderEmpty.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builderEmpty.create();
                            alertDialog.show();
                        }
                    }else {
                        AlertDialog.Builder builderEmpty = new AlertDialog.Builder(Register.this);
                        builderEmpty.setMessage("邮箱已注册");
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

            private boolean isNumber(String age) {
                for (int i = 0; i < age.length(); i++) {
                    if (!Character.isDigit(age.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }

            private boolean isEmail(String email) {
                for (Map.Entry<String,user> entry : AllData.userData.entrySet()){
                    if (entry.getValue().getUserEmail().equals(email)){
                        return false;
                    }
                }
                return true;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, LogUi.class);
                startActivity(intent);

                Register.this.finish();
            }
        });
    }

    private void insertUser(String email, String password, int parseInt, String sex, String phone,String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement pre = AllData.connection.prepareStatement("INSERT INTO users VALUES(?,?,?,?,?,?,?)");
                    String userCode = isCode();
                    pre.setString(1,userCode);
                    pre.setString(2,email);
                    pre.setString(3,password);
                    pre.setString(4,name);
                    pre.setInt(5,parseInt);
                    pre.setString(6,sex);
                    pre.setString(7,phone);

                    pre.executeUpdate();
                    pre.close();

                    user user = new user(email, password, name, parseInt, sex, phone);
                    AllData.userData.put(userCode,user);
                    AllData.userCodes.add(userCode);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Message msg = Message.obtain();
                msg.what = 0;
                handler.sendMessage(msg);
            }

            private String isCode() {
                if (AllData.userCodes.isEmpty()){
                    return "#U1";
                }else {
                    int size = AllData.userCodes.size();
                    String s = AllData.userCodes.get(size - 1);
                    return "#U" + (Integer.parseInt(s.substring(2))+1);
                }

            }
        }).start();
    }

    private void initRe() {
        button = findViewById(R.id.reButton);
        getReEmail = findViewById(R.id.reEmail);
        getRePassword = findViewById(R.id.rePassword);
        getRePasswordCont = findViewById(R.id.rePasswordCont);
        getReAge = findViewById(R.id.reAge);
        getReName = findViewById(R.id.reName);
        getReMan = findViewById(R.id.radio_man);
        getReWoman = findViewById(R.id.radio_woman);
        getRePhone = findViewById(R.id.rePhone);
        backButton = findViewById(R.id.rebackButton);
    }
}
