package com.example.userapp;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class overall_main extends AppCompatActivity {

    private static final int REQUEST_CODE_BLUETOOTH = 1;
    private static final int REQUEST_CODE_BLUETOOTH_ADM = 2;
    static BlueToothController mController = new BlueToothController();
    private Toast mToast;
    private Button  drugButton;
    private Button myButton;
    private ImageView imageMyDrug_Main;
    private ImageView imageHoisDrug_Main;
    private ImageView imageGetDrug_Main;
    private ImageView imageShuoMin_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overall_interface);
        
        initOverAll();

        drugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(overall_main.this, userMyUI.class);
                startActivity(intent);
                overall_main.this.finish();
            }
        });

        imageMyDrug_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(overall_main.this, userUI.class);
                startActivity(intent);
                overall_main.this.finish();
            }
        });

        imageGetDrug_Main.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                boolean ret = mController.getBlueToothStatus();
                if (!ret){
                    requestTurnOnBlueTooth();
                }else {
                    @SuppressLint("MissingPermission")
                    int scanMode = mController.getmAdapter().getScanMode();
                    if (scanMode == mController.getmAdapter().SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                        // 设备具有可见性，且其他设备可以发现该设备
                        // 进行你的逻辑处理
                        Intent intent = new Intent(overall_main.this, getDrugUI.class);
                        startActivity(intent);
                        overall_main.this.finish();
                    } else {
                        // 设备不具有可见性，或者其他设备无法发现该设备
                        // 进行你的逻辑处理
                        Intent discoverableIntent = new Intent(mController.getmAdapter().ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(mController.getmAdapter().EXTRA_DISCOVERABLE_DURATION, 300);
                        startActivityForResult(discoverableIntent, REQUEST_CODE_BLUETOOTH_ADM);
                    }
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BLUETOOTH){
            if (resultCode == RESULT_OK) {
                Intent discoverableIntent = new Intent(mController.getmAdapter().ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(mController.getmAdapter().EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(discoverableIntent, REQUEST_CODE_BLUETOOTH_ADM);
            } else {
                // 用户未打开蓝牙，处理相应逻辑
                Toast.makeText(overall_main.this, "请打开蓝牙", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initOverAll() {
        drugButton = findViewById(R.id.drugButton);
        myButton = findViewById(R.id.myButton);
        imageHoisDrug_Main = findViewById(R.id.imageHoisDrug_Main);
        imageMyDrug_Main = findViewById(R.id.imageMyDrug_Main);
        imageGetDrug_Main = findViewById(R.id.imageGetDrug_Main);
        imageShuoMin_Main = findViewById(R.id.imageShuoMin_Main);
    }


    public void isSupportBlueTooth(){
        boolean ret = mController.isSupportBlueTooth();
        showToat("suppport BlueTooth?"+ret);
    }

    public void requestTurnOnBlueTooth(){
        mController.turnOnBlueTooth(overall_main.this,REQUEST_CODE_BLUETOOTH);
    }

    public void turnOffBlueTooth(){
        mController.turnOffBlueTooth();
    }

    private void showToat(String text){
        if (mToast == null){
            mToast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        }else {
            mToast.setText(text);
        }
        mToast.show();
    }

}
