package com.example.partytricks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

public class SodaActivity extends AppCompatActivity {

    private SensorManager mysm;
    private AudioManager amgr;
    private Sensor myS, mySGrav;
    //private Sensor mySGrav;
    private Vibrator vibrator;
    private Button btn_return;
    private ImageView mImageView;
    private TextView tv1, tv2, tv3, tv4;
    private SoundPool soundPool;
    private MediaPlayer mMediaPlayer;
    private HashMap<Integer, Integer> soundPoolMap;
    private int count = 0;
    private int full;
    private int current = 0;
    private boolean flagFull = false;
    private boolean flagPass = false;//test git
    private boolean flagShake = false;
    private boolean flagKeyDown = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soda);
        mImageView = findViewById(R.id.soda_glass);
        vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        mysm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myS = mysm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mySGrav = mysm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        count = 0;
        btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SodaActivity.this,SettingActivity.class);
                startActivity(intent);
                //mMediaPlayer.stop();
            }
        });
        full = create_Random();
        initSounds();
        mMediaPlayer.start();
    }
    public void initSounds(){
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1,soundPool.load(this, R.raw.soda,1));
//        soundPoolMap.put(2,soundPool.load(this, R.raw.soda,2));
//        soundPoolMap.put(3,soundPool.load(this, R.raw.soda,3));
        mMediaPlayer = MediaPlayer.create(this, R.raw.background_soda);
    }

    public void playSound(int sound, int loop){
        amgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = amgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = amgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent/streamVolumeMax;
        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }

    private SensorEventListener mySel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] value = event.values;
//            tv1.setText("X轴加速度" + value[0]);
//            tv2.setText("Y轴加速度" + value[1]);
//            tv3.setText("Z轴加速度" + value[2]);
            tv4.setText("count" + count);
            if((value[1] > 8 || value[1] < -8) && !flagFull){
                count ++;
                current = count;
                if(flagShake){
                    playSound(1,0);
                    vibrator.vibrate(new long[]{0,0,10,5000}, -1);
                    flagShake = false;
                }
                //receiveMessage();
            }
            else{
                vibrator.cancel();
                flagShake = true;
            }
            if(count > full){
                //mImageView.setBackground(getResources().getDrawable(R.raw.dd));
                mMediaPlayer.stop();
                flagFull = true;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private SensorEventListener mySelGrav = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] value = event.values;
            tv1.setText("X轴加速度" + value[0]);
            tv2.setText("Y轴加速度" + value[1]);
            tv3.setText("Z轴加速度" + value[2]);
            if((value[1] < -9) && !flagFull && flagKeyDown){
                String message = sendMessage();
                //send
                ((ServiceOnBack)getApplication()).sendMessage(message.getBytes());
                flagKeyDown = false;
            }
//            else{
//                vibrator.cancel();
//                flagShake = true;
//            }
//            if(count > full){
//                //mImageView.setBackground(getResources().getDrawable(R.raw.dd));
//                mMediaPlayer.stop();
//                flagFull = true;
//                sendMessage();
//            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    protected void onResume(){
        super.onResume();
        mysm.registerListener(mySel, myS, SensorManager.SENSOR_DELAY_NORMAL);
        mysm.registerListener(mySelGrav, mySGrav, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mysm.unregisterListener(mySel);
        mysm.unregisterListener((mySelGrav));
        mMediaPlayer.stop();
    }

    private int create_Random(){
        Random rand = new Random();
        return rand.nextInt(400) + 500;
    }

    private String sendMessage(){
        int send_current = current;
        int send_full = full;
        String message = String.valueOf(send_current) + "/" + String.valueOf(send_full);
        for(int i = send_current; i >=0; i-=10){
            count = i;
        }
        current = 0;
        count = 0;
        return message;
    }
    private void receiveMessage(){
        String message = "500/600";
        String[] splitMessage=message.split("/");
        int receive_current = Integer.parseInt(splitMessage[0]);
        int receive_full = Integer.parseInt(splitMessage[1]);
        for(int i = current; i < receive_current; i+=10){
//            try {
//                Thread.sleep(5 * 10); //设置暂停的时间 5 秒
//                //current = i;
            count = i;
//                tv4.setText("count" + count);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        current = receive_current;
        full = receive_full;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager am = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                //am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, 0);
                am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_SAME, 0);
//                bang.setText("Bang_up!");
                flagKeyDown = true;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                //am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, 0);
                am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_SAME, 0);
//                defense.setText("Defense!");
                flagKeyDown = true;
                break;
            default:
                flagKeyDown = false;
                break;
        }
        return true;
        //return super.onKeyDown(keyCode, event);
    }
}
