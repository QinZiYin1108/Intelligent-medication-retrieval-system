package com.example.userapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * 蓝牙适配器
 */
public class BlueToothController {
    private BluetoothAdapter mAdapter;

    public BlueToothController() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getmAdapter() {
        return mAdapter;
    }

    /**
     * 是否支持蓝牙
     * @return
     */
    public boolean isSupportBlueTooth(){
        if (mAdapter!=null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断当前蓝牙状态
     */
    public boolean getBlueToothStatus(){
        assert (mAdapter!=null);
        return mAdapter.isEnabled();
    }

    @SuppressLint("MissingPermission")
    public void turnOnBlueTooth(Activity activity, int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent,requestCode);
    }


    @SuppressLint("MissingPermission")
    public void turnOffBlueTooth() {
        mAdapter.disable();
    }
}
