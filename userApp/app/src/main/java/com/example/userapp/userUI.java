package com.example.userapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class userUI extends AppCompatActivity {
    private RecyclerView viewById;
    private  SmartRefreshLayout smart;
    private ArrayList<record> records;
    private ArrayList<String> recordCodes;
    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_ui);

        initUser();

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userUI.this, overall_main.class);
                startActivity(intent);
                userUI.this.finish();
            }
        });

        records = new ArrayList<>();
        recordCodes = new ArrayList<>();
        for(String code : AllData.recordCode){
            if (AllData.recordData.get(code).getReType().equals("未支付")){
                records.add(AllData.recordData.get(code));
                recordCodes.add(code);
            }
        }
        TestAdapter testAdapter = new TestAdapter(records);
        viewById.setAdapter(testAdapter);

        testAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ArrayList<String> recordCodes1 = recordCodes;
                String s = recordCodes.get(position);
                AllData.trueRecordCoded = s;
                Intent intent = new Intent(userUI.this, recodUI.class);
                startActivity(intent);
                userUI.this.finish();
            }
        });

        smart.setEnableLoadmore(false);

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Thread shua = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AllData.readRecord(AllData.trueUserCode);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                shua.start();

                try {
                    shua.join();

                    records = new ArrayList<>();
                    recordCodes = new ArrayList<>();
                    for(String code : AllData.recordCode){
                        if (AllData.recordData.get(code).getReType().equals("未支付")){
                            records.add(AllData.recordData.get(code));
                            recordCodes.add(code);
                        }
                    }
                    testAdapter.setNewData(records);
                    testAdapter.notifyDataSetChanged();
                    smart.finishRefresh();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    private void initUser() {
        smart = (SmartRefreshLayout) findViewById(R.id.smartDrug);
        viewById = (RecyclerView) findViewById(R.id.rv);
        imageBack = findViewById(R.id.imageBack);
    }
}
