package com.example.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DrugShipmentUI extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_shipment_ui);

        donghua_Shipment();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (recordDrug recordDrug : getDrug_record_UI.recordDrugs){
                    if (recordDrug.getDgCode().equals("#DG1")){
                        BlueTooth_ADM_UI.sendMessage("1");
                    } else if (recordDrug.getDgCode().equals("#DG2")) {
                        BlueTooth_ADM_UI.sendMessage("2");
                    } else if (recordDrug.getDgCode().equals("#DG3")) {
                        BlueTooth_ADM_UI.sendMessage("3");
                    } else if (recordDrug.getDgCode().equals("#DG4")) {
                        BlueTooth_ADM_UI.sendMessage("4");
                    }
                    BlueTooth_ADM_UI.sendMessage(String.valueOf(recordDrug.getRdNumber()));
                }
                BlueTooth_ADM_UI.sendMessage("0");

                while (true) {
                    // 循环执行的代码
                    try {
                        if (BlueTooth_ADM_UI.BlueTooth_key){
                            getDrug_record_UI.thread.join();
                            Intent intent = new Intent(DrugShipmentUI.this, getDrugUI.class);
                            startActivity(intent);
                            DrugShipmentUI.this.finish();
                            BlueTooth_ADM_UI.disconnectBluetooth();
                            break;
                        }
                        // 线程休眠500毫秒
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread1.start();
    }

    private static void donghua_Shipment() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000); // 动画持续时间为1秒
        rotateAnimation.setRepeatCount(Animation.INFINITE); // 设置动画重复次数为无限
        rotateAnimation.setInterpolator(new LinearInterpolator()); // 设置动画插值器为线性插值器
    }
}
