package com.example.partytricks;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SettingBluetoothActivity extends Activity{
    private BluetoothAdapter btAdapter = null;// 本地蓝牙适配器
    private MyService myService = null;// Service引用
    private Button sendBtn,getDeviceBtn;
    private String connectedNameStr = null;// 已连接的设备名称
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // 获取本地蓝牙适配器
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    @Override
    protected void onStart() {
        super.onStart();
        // 如果蓝牙没有开启，提示开启蓝牙，并退出Activity
        if(!btAdapter.isEnabled()){
            Toast.makeText(this, "请先开启蓝牙！", Toast.LENGTH_LONG).show();
            finish();
        }else{//否则初始化聊天控件
            if(myService==null){
                initChat();
            }
        }
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if (myService == null) {// 创建并开启Service
            // 如果Service为空状态
            if (myService.getState() == MyService.STATE_NONE) {
                myService.start();// 开启Service
            }
        }
    }
    @Override
    public synchronized void onPause(){
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myService != null) {// 停止Service
            ((ServiceOnBack)getApplication()).setSocket(myService.getSocket());
//            myService.stop();
        }
    }
    private void initChat(){
        sendBtn=(Button)this.findViewById(R.id.btn_send);//获取按钮的引用
        sendBtn.setOnClickListener(new OnClickListener(){//为发送按钮添加监听器
            @Override
            public void onClick(View v) {
                //获得编辑文本框的文本内容，并发送消息
                String msg="<DATA STRUCTURE>";
                sendMessage(msg);
            }});
        getDeviceBtn = (Button)this.findViewById(R.id.btn_getDevice);
        getDeviceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(SettingBluetoothActivity.this, MyDeviceListActivity.class);
                startActivityForResult(serverIntent, Constant.REQUEST_CODE);
            }
        });
        myService = new MyService(this, mHandler);// 创建Service对象
    }
    private void sendMessage(String msg){
        // 先检查是否已经连接到设备
        if (myService.getState() != MyService.STATE_CONNECTED) {
            Toast.makeText(this, "未连接到设备！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(msg.length()>0){//设备已经连接
            byte[] send = msg.getBytes();// 获取发送消息的字节数组
            myService.write(send);//发送
        }
    }
    public class MyHandler extends Handler implements Serializable{
        private static final long serialVersionUID = 5459584623637095611L;
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case Constant.MSG_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // 创建接收的信息的字符串
                    String readMessage = new String(readBuf, 0, msg.arg1);//用系统默认的字符集把字节数组的一个序列转换为字符串
                    //send Broadcast
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(SodaActivity.SodaReceiver.SODA_ACCEPT_RESPONSE);
                    broadcastIntent.putExtra(SodaActivity.RESPONSE_STRING,readMessage);
                    sendBroadcast(broadcastIntent);

                    Toast.makeText(getApplicationContext(),
                            connectedNameStr + ":  " + readMessage,
                            Toast.LENGTH_LONG).show();//显示从哪个设备接收的什么样的字符串
                    break;
                case Constant.MSG_DEVICE_NAME:
                    // 获取已连接的设备名称，并弹出提示信息
                    connectedNameStr = msg.getData().getString(
                            Constant.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "已连接到 " + connectedNameStr, Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    }
    // 处理从Service发来的消息的Handler
    private final MyHandler mHandler = new MyHandler();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE:               //data包含了返回数据
                // 如果设备列表Activity返回一个连接的设备
                if (resultCode == Activity.RESULT_OK) {
                    // 获取设备的MAC地址
                    String address = data.getExtras().getString(
                            MyDeviceListActivity.EXTRA_DEVICE_ADDR);
                    // 获取BluetoothDevice对象,根据给出的address
                    BluetoothDevice device = btAdapter.getRemoteDevice(address);
                    myService.connect(device);// 连接该设备
                }
                break;
        }
    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        menu.clear();
//        // 启动设备列表Activity搜索设备
//
//        return super.onPrepareOptionsMenu(menu);
//    }
}
