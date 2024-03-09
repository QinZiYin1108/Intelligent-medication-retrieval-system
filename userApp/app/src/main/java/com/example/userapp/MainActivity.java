package com.example.userapp;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView init_text_view;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            if (msg.what==0){
                MainActivity.this.finish();

                Intent intent = new Intent(MainActivity.this, LogUi.class);
                startActivity(intent);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (AllData.logKey){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            String sqlUser = "SELECT COUNT(*) FROM users";
                            String sqlDrug = "SELECT COUNT(*) FROM drug";
                            try {
                                PreparedStatement pre = AllData.connection.prepareStatement(sqlUser);
                                ResultSet resultSet = pre.executeQuery();
                                resultSet.next();
                                int userCount = resultSet.getInt(1);
                                resultSet.close();
                                pre.close();

                                PreparedStatement pre1 = AllData.connection.prepareStatement(sqlDrug);
                                ResultSet resultSet1 = pre1.executeQuery();
                                resultSet1.next();
                                int drugCount = resultSet1.getInt(1);
                                resultSet1.close();
                                pre1.close();

                                if (userCount!=AllData.userCodes.size()){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                AllData.readUser();
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }).start();
                                }

                                if (drugCount!=AllData.drugCode.size()){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                AllData.readDrug();
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }).start();
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }).start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = findViewById(R.id.progressBar);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000); // 动画持续时间为1秒
        rotateAnimation.setRepeatCount(Animation.INFINITE); // 设置动画重复次数为无限
        rotateAnimation.setInterpolator(new LinearInterpolator()); // 设置动画插值器为线性插值器

        init_text_view = findViewById(R.id.initText);

        initMySql();
    }

    private void initMySql() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean key;
                try {
                    key = AllData.getConnection();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                try {
                    AllData.readUser();
                    AllData.readDrug();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (key){
                    Message msg = Message.obtain();
                    msg.what = 0;

                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

}

