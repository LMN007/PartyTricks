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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

public class SodaActivity extends AppCompatActivity {

    private SensorManager mysm, mysmGrav;
    private AudioManager amgr;
    private Sensor myS;
    private Sensor mySGrav;
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
    private boolean flagPass = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soda);
        mImageView = findViewById(R.id.soda_glass);
        mysm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myS = mysm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mySGrav = mysmGrav.getDefaultSensor(Sensor.TYPE_GRAVITY);
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
                mMediaPlayer.stop();
            }
        });
        full = create_Random();
        initSounds();
        mMediaPlayer.start();
    }
    public void initSounds(){
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1,soundPool.load(this, R.raw.soda,1));
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
            tv1.setText("X轴加速度" + value[0]);
            tv2.setText("Y轴加速度" + value[1]);
            tv3.setText("Z轴加速度" + value[2]);
            tv4.setText("count" + count);
            if(flagPass){
                count = 500;
                full = 600;
                flagPass = false;
            }
            if((value[1] > 8 || value[1] < -8) && !flagFull){
                count ++;
                playSound(1,0);
                Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{10,1,10,count}, -1);
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

    @Override
    protected void onResume(){
        super.onResume();
        mysm.registerListener(mySel, myS, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mysm.unregisterListener(mySel);
        mMediaPlayer.stop();
        //300/500
    }

    private int create_Random(){
        Random rand = new Random();
        return rand.nextInt(400) + 500;
    }
}
