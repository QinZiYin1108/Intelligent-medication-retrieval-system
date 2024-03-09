package com.example.userapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BlueTooth_ADM_UI extends AppCompatActivity {
    public static boolean BlueTooth_key = false;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice targetDevice;
    private static BluetoothSocket bluetoothSocket;
    private static OutputStream outputStream;
    private InputStream inputStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_jiazai);
        donghua();

        bluetoothAdapter = overall_main.mController.getmAdapter();
        searchAndConnectToDevice();
    }


    @SuppressLint("MissingPermission")
    private void searchAndConnectToDevice() {
        // 取消之前的搜索
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        // 开始搜索蓝牙设备
        bluetoothAdapter.startDiscovery();
    }

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // 当找到设备时
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // 判断设备名称是否为目标设备名称，此处为"#MH1"
                if (device!=null){
                    if (device.getAddress().equals(getDrugUI.BlueTooth_ADM)) {
                        targetDevice = device; // 找到目标设备
                        connectToDevice();
                    }
                }
            }
        }
    };

    private void connectToDevice() {
        // 在连接操作中建立蓝牙连接
        ConnectThread connectThread = new ConnectThread(targetDevice);
        connectThread.start();
    }

    private class ConnectThread extends Thread {
        private BluetoothDevice device;

        public ConnectThread(BluetoothDevice device) {
            this.device = device;
        }

        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            try {
                // 建立蓝牙连接
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();

                // 获取输入输出流
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();

                // 在一个单独的线程中持续读取输入流
                new ReceiveThread().start();

                Intent intent = new Intent(BlueTooth_ADM_UI.this, getDrug_record_UI.class);
                startActivity(intent);
                BlueTooth_ADM_UI.this.finish();

            } catch (IOException e) {
                e.printStackTrace();
                // 处理连接失败的情况
            }
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            try {
                while (true) {
                    bytes = inputStream.read(buffer);
                    String receivedMessage = new String(buffer, 0, bytes);

                    // 处理接收到的消息

                    BlueTooth_key = true;

                }
            } catch (IOException e) {
                e.printStackTrace();
                // 处理读取异常的情况
            }
        }
    }

    public static void sendMessage(String message) {
        try {
            // 将文本转换为字节数组
            byte[] bytes = message.getBytes();
            // 发送消息
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            // 处理发送消息失败的情况
        }
    }

    public static void disconnectBluetooth() {
        if (bluetoothSocket != null) {
            try {
                // 关闭蓝牙连接
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                // 处理关闭连接失败的情况
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 注册蓝牙广播接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 取消注册蓝牙广播接收器
        unregisterReceiver(bluetoothReceiver);
    }

    private static void donghua() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000); // 动画持续时间为1秒
        rotateAnimation.setRepeatCount(Animation.INFINITE); // 设置动画重复次数为无限
        rotateAnimation.setInterpolator(new LinearInterpolator()); // 设置动画插值器为线性插值器
    }
}
