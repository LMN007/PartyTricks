package com.example.partytricks;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ServiceOnBack extends Application {
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void rmSocket(){
        try {
            socket.close();
            //bts maybe bug
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public BluetoothSocket getSocket(){return socket;}
    public void setSocket(BluetoothSocket socket){
        this.socket = socket;
        try{
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }catch(Exception e){e.printStackTrace();}
    }
    public synchronized void sendMessage(byte[] buffer){
        if(outputStream!=null){
            try {
                outputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "there is no back connection", Toast.LENGTH_SHORT).show();
        }
    }
}