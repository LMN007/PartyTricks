package com.example.partytricks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyService implements Serializable{
    private static final long serialVersionUID = 5459584623637090823L;
    public enum ConnType{
        SEND,
        ACCEPT,
    }
    // 本应用的唯一 UUID,全局唯一标识
    private static final UUID MY_UUID = UUID.
            fromString("db764ac8-4b08-7f25-aafe-59d03c27bae3");
    //设定本蓝牙连接类型
    private ConnType connType;
    // 成员变量
    private final BluetoothAdapter btAdapter;
    private final Handler myHandler;
    private AcceptThread myAcceptThread;
    private ConnectThread myConnectThread;
    private ConnectedThread myConnectedThread;

    private BluetoothSocket bts;
    private int myState;
    // 表示当前连接状态的常量
    public static final int STATE_NONE = 0;       // 什么也没做
    public static final int STATE_LISTEN = 1;     // 正在监听连接
    public static final int STATE_CONNECTING = 2; // 正在连接
    public static final int STATE_CONNECTED = 3;  // 已连接到设备
    // 构造器
    public MyService(Context context, Handler handler,ConnType type) {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myState = STATE_NONE;
        myHandler = handler;
        this.connType = type;
    }
    //设置当前连接状态的方法
    private synchronized void setState(int state) {
        myState = state;
    }
    //获取当前连接状态的方法
    public synchronized int getState() {
        return myState;
    }

    public synchronized BluetoothSocket getSocket(){return bts;}
    //开启service的方法
    public synchronized void start() {
        // 关闭不必要的线程
        if (myConnectThread != null) {
            myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {
            myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread == null&&this.connType==ConnType.ACCEPT) {// 开启线程监听连接(修改指定为accept类型才开启)
            myAcceptThread = new AcceptThread();
            myAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }
    //连接设备的方法
    public synchronized void connect(BluetoothDevice device) {
        if(this.connType == ConnType.ACCEPT) return;//只有指定为send类型才可以连接
        // 关闭不必要的线程
        if (myState == STATE_CONNECTING) {
            if (myConnectThread != null) {
                myConnectThread.cancel(); myConnectThread = null;}
        }
        if (myConnectedThread != null) {
            myConnectedThread.cancel(); myConnectedThread = null;}
        // 开启线程连接设备
        myConnectThread = new ConnectThread(device);
        myConnectThread.start();
        setState(STATE_CONNECTING);
    }
    //开启管理和已连接的设备间通话的线程的方法
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // 关闭不必要的线程
        if (myConnectThread != null) {
            myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {
            myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread != null) {
            myAcceptThread.cancel(); myAcceptThread = null;}
        // 创建并启动ConnectedThread
        myConnectedThread = new ConnectedThread(socket);
        myConnectedThread.start();
        // 发送已连接的设备名称到主界面Activity
        Message msg = myHandler.obtainMessage(Constant.MSG_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        myHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }
    public synchronized void stop() {//停止所有线程的方法
        if (myConnectThread != null) {
            myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {
            myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread != null) {
            myAcceptThread.cancel(); myAcceptThread = null;}
        setState(STATE_NONE);
    }
    public void write(byte[] out) {//向ConnectedThread写入数据的方法
        ConnectedThread tmpCt;// 创建临时对象引用
        synchronized (this) {// 锁定ConnectedThread
            if (myState != STATE_CONNECTED) return;
            tmpCt = myConnectedThread;
        }
        tmpCt.write(out);// 写入数据
    }
    private class AcceptThread extends Thread implements Serializable{//用于监听连接请求的线程
        // 本地服务器端ServerSocket
        private static final long serialVersionUID = 5459584623637095610L;
        private final BluetoothServerSocket mmServerSocket;
        public AcceptThread() {
            BluetoothServerSocket tmpSS = null;
            try {// 创建用于监听的服务器端ServerSocket
                tmpSS = btAdapter.
                        listenUsingRfcommWithServiceRecord("BluetoothChat", MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmServerSocket = tmpSS;
        }
        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            while (myState != STATE_CONNECTED) {//如果没有连接到设备
                try {
                    socket = mmServerSocket.accept();//获取连接的Sock
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                if (socket != null) {// 如果连接成功
                    synchronized (MyService.this) {
                        switch (myState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // 开启管理连接后数据交流的线程
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                try {// 关闭新Socket
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }}
            }
        }
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //用于尝试连接其他设备的线程
    private class ConnectThread extends Thread implements Serializable{
        private static final long serialVersionUID = 5459584623637094262L;
        private final BluetoothSocket myBtSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // 通过正在连接的设备获取BluetoothSocket
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myBtSocket = tmp;
        }
        public void run() {
            setName("ConnectThread");
            btAdapter.cancelDiscovery();// 取消搜索设备
            try {// 连接到BluetoothSocket
                myBtSocket.connect();//尝试连接
            } catch (IOException e) {
                setState(STATE_LISTEN);//连接断开后设置状态为正在监听
                try {// 关闭socket
                    myBtSocket.close();
                } catch (IOException e2) {
                    e.printStackTrace();
                }
                MyService.this.start();//如果连接不成功，重新开启service
                return;
            }
            synchronized (MyService.this) {// 将ConnectThread线程置空
                myConnectThread = null;
            }
            connected(myBtSocket, mmDevice);// 开启管理连接后数据交流的线程
        }
        public void cancel() {
            try {
                myBtSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //用于管理连接后数据交流的线程
    private class ConnectedThread extends Thread implements Serializable{
        private static final long serialVersionUID = 5459584623637094111L;
        private final BluetoothSocket myBtSocket;
        private final InputStream mmInStream;
        private final OutputStream myOs;
        public ConnectedThread(BluetoothSocket socket) {
            myBtSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // 获取BluetoothSocket的输入输出流
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            myOs = tmpOut;
            bts = myBtSocket;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {// 一直监听输入流
                try {
                    bytes = mmInStream.read(buffer);// 从输入流中读入数据
                    //将读入的数据发送到主Activity
                    myHandler.obtainMessage(Constant.MSG_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    setState(STATE_LISTEN);//连接断开后设置状态为正在监听
                    break;
                }
            }
        }
        //向输出流中写入数据的方法
        public void write(byte[] buffer) {
            try {
                myOs.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void cancel() {
            try {
                myBtSocket.close();
                //bts maybe bug
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
