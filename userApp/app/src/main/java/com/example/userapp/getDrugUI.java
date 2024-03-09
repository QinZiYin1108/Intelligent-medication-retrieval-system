package com.example.userapp;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class getDrugUI extends AppCompatActivity {
    static String BlueTooth_ADM = null;
    private BluetoothDevice targetDevice = null; // 指定的蓝牙设备
    private ImageView backButtonT;
    private ArrayList<record> records;
    private RecyclerView viewById;
    private ArrayList<String> recordCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getdrugui);
        initGetDrug();


        backButtonT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getDrugUI.this, overall_main.class);
                startActivity(intent);
                getDrugUI.this.finish();
            }
        });

        recordCodes = new ArrayList<>();
        records = new ArrayList<>();
        for(String code : AllData.recordCode){
            if (AllData.recordData.get(code).getReType().equals("已付款")){
                records.add(AllData.recordData.get(code));
                recordCodes.add(code);
            }
        }
        TestAdapter testAdapter = new TestAdapter(records);
        viewById.setAdapter(testAdapter);

        testAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (overall_main.mController.getBlueToothStatus()){
                    AllData.trueRecordCoded = recordCodes.get(position);
                    IntentIntegrator intentIntegrator = new IntentIntegrator(getDrugUI.this);
                    intentIntegrator.initiateScan();
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    intentIntegrator.setPrompt("将机器二维码放置取景框内");
                }else {
                    Toast.makeText(getDrugUI.this, "蓝牙未打开", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        super.onActivityResult(requestCode, resultCode, data);
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(getDrugUI.this, "取消扫描", Toast.LENGTH_LONG).show();
                } else {
                    BlueTooth_ADM = result.getContents().split("/")[1];
                    AllData.trueMachineCode = result.getContents().split("/")[0];
                    Intent intent = new Intent(getDrugUI.this, BlueTooth_ADM_UI.class);
                    startActivity(intent);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);}
    }

    private void initGetDrug() {
        backButtonT = findViewById(R.id.imageBack_three);
        viewById = findViewById(R.id.rv_record_getDrug);
    }


}
