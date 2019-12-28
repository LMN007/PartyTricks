package com.example.partytricks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.particle.ParticleGroupType;
import org.jbox2d.particle.ParticleSystem;
import org.jbox2d.particle.ParticleType;

import com.example.partytricks.R;
import com.example.partytricks.box2d.software.GameView;
import com.example.partytricks.box2d.software.MyBody;
import com.example.partytricks.box2d.software.MyPolygonColor;
import com.example.partytricks.box2d.util.Box2DUtil;
import com.example.partytricks.box2d.util.Constant;
import com.example.partytricks.box2d.util.WaterObject;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import static com.example.partytricks.box2d.util.Constant.*;

public class SodaActivity extends AppCompatActivity {
    public static final String RESPONSE_STRING = "soda_is_accepting";
    private SodaReceiver sodaReceiver;

    private SensorManager mysm;
    private AudioManager amgr;
    private Sensor myS, mySGrav;
    //private Sensor mySGrav;
    private Vibrator vibrator;
    private Button btn_return;
    private ImageView mImageView;
    private TextView tv1, tv2, tv3, tv4;
    private SoundPool soundPool, soundPool2;
    private MediaPlayer mMediaPlayer;
    private HashMap<Integer, Integer> soundPoolMap, soundPoolMapBreak;
    private int count = 0;
    private int full;
    private int current = 0;
    private boolean flagFull = false;
    private boolean flagBreak = false;//test git
    private boolean flagShake = false;
    private boolean flagKeyDown = false;

    public World world;
    public ParticleSystem m_particleSystem;//声明流体粒子系统
    //物体列表
    public ArrayList<MyBody> bl=new ArrayList<MyBody>();
    public Bitmap[] waters=new Bitmap[2];
    public int watersIndex = 0;
    public Bitmap sodaBackground;
    public Bitmap waterMask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.partytricks.Constant.setSodaBreak(com.example.partytricks.Constant.SodaBreak.SODA_0);
        com.example.partytricks.Constant.setSodaState(com.example.partytricks.Constant.SodaState.SODA_IN);
        IntentFilter filter = new IntentFilter(SodaReceiver.SODA_ACCEPT_RESPONSE);
        sodaReceiver = new SodaReceiver();
        registerReceiver(sodaReceiver,filter);

        //UI
//        mImageView = findViewById(R.id.soda_glass);
        vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        mysm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myS = mysm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mySGrav = mysm.getDefaultSensor(Sensor.TYPE_GRAVITY);
//        tv1 = findViewById(R.id.tv1);
//        tv2 = findViewById(R.id.tv2);
//        tv3 = findViewById(R.id.tv3);
//        tv4 = findViewById(R.id.tv4);
        count = 0;
//        btn_return = findViewById(R.id.btn_return);
//        btn_return.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(SodaActivity.this,SettingActivity.class);
//                startActivity(intent);
//                //mMediaPlayer.stop();
//            }
//        });
        full = create_Random();
        initSounds();
        mMediaPlayer.start();

        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        //设置为竖屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //获取屏幕尺寸
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<dm.heightPixels)
        {
            SCREEN_WIDTH=dm.widthPixels;
            SCREEN_HEIGHT=dm.heightPixels;
        }
        else
        {
            SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;
        }
        Constant.ScaleSR();//屏幕自适应

        initBitmap(this.getResources());//初始化图片
        Vec2 gravity = new Vec2(com.example.partytricks.Constant.accelerateX,com.example.partytricks.Constant.accelerateY);
        //创建世界
        world = new World(gravity);

        //创建地面
        final int kd=1;//宽度或高度
        //创建包围框
        MyPolygonColor mrc=Box2DUtil.createBox//最底部
                (
                        0,
                        SCREEN_HEIGHT-kd,
                        new float[][]
                                {
                                        {0,0},{SCREEN_WIDTH,0},{SCREEN_WIDTH,kd},{0,kd}
                                },
                        true,
                        world,
                        Color.BLACK,
                        0,0,0,0
                );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最上面
                (
                        0,
                        0,
                        new float[][]
                                {
                                        {0,0},{SCREEN_WIDTH,0},{SCREEN_WIDTH,kd},{0,kd}
                                },
                        true,
                        world,
                        Color.BLACK,
                        1,0,0,0
                );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最右边
                (
                        SCREEN_WIDTH-kd,
                        0,
                        new float[][]
                                {
                                        {0,0},{kd,0},{kd,SCREEN_HEIGHT},{0,SCREEN_HEIGHT}
                                },
                        true,
                        world,
                        Color.BLACK,
                        2,0,0,0
                );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最左边
                (
                        0,
                        0,
                        new float[][]
                                {
                                        {0,0},{kd,0},{kd,SCREEN_HEIGHT},{0,SCREEN_HEIGHT}
                                },
                        true,
                        world,
                        Color.BLACK,
                        3,0,0,0
                );
        bl.add(mrc);

        m_particleSystem=world.m_particleSystem;
        m_particleSystem.setParticleRadius(1.6f);//设置粒子的半径
        m_particleSystem.setParticleDamping(0.4f);//设置粒子的潮湿因子
        m_particleSystem.setParticleGravityScale(10);

        WaterObject.createWaterRectObject((380+x)*ratio,(680+y)*ratio,500*ratio,500*ratio,2,ParticleType.b2_waterParticle,ParticleGroupType.b2_solidParticleGroup,m_particleSystem,0);//创建具有水属性的流体

        GameView gv= new GameView(SodaActivity.this);
        setContentView(gv);

    }
    public void initSounds(){
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
        soundPool2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMapBreak = new HashMap<Integer, Integer>();
        soundPoolMap.put(1,soundPool.load(this, R.raw.soda,1));
        soundPoolMapBreak.put(1,soundPool2.load(this,R.raw.bg,1));
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

    public void playSoundBreak(int sound, int loop){
        amgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = amgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = amgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent/streamVolumeMax;
        soundPool2.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }

    private SensorEventListener mySel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] value = event.values;
            com.example.partytricks.Constant.accelerateX = value[0];
            com.example.partytricks.Constant.accelerateY = value[1];
//            tv1.setText("X轴加速度" + value[0]);
//            tv2.setText("Y轴加速度" + value[1]);
//            tv3.setText("Z轴加速度" + value[2]);
//            tv4.setText("count" + count);
            if((value[1] > 8 || value[1] < -8) && !flagFull && com.example.partytricks.Constant.getSodaState() == com.example.partytricks.Constant.SodaState.SODA_IN){
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
            if(count > 0.4*full && count < 0.8*full){
                com.example.partytricks.Constant.setSodaBreak(com.example.partytricks.Constant.SodaBreak.SODA_40);
            }
            if(count > 0.8*full && count < full){
                com.example.partytricks.Constant.setSodaBreak(com.example.partytricks.Constant.SodaBreak.SODA_80);
            }
            if(count >= full){
                //mImageView.setBackground(getResources().getDrawable(R.raw.dd));
                com.example.partytricks.Constant.setSodaBreak(com.example.partytricks.Constant.SodaBreak.SODA_100);
                com.example.partytricks.Constant.setSodaState(com.example.partytricks.Constant.SodaState.SODA_BREAK);
                mMediaPlayer.stop();
                flagFull = true;
                if(!flagBreak){
                    playSoundBreak(1,0);
                    flagBreak = true;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private SensorEventListener mySelGrav = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] value = event.values;
//            tv1.setText("X轴加速度" + value[0]);
//            tv2.setText("Y轴加速度" + value[1]);
//            tv3.setText("Z轴加速度" + value[2]);
            if((value[1] < -5) && !flagFull && flagKeyDown && com.example.partytricks.Constant.getSodaState() == com.example.partytricks.Constant.SodaState.SODA_IN){
                String message = sendMessage();
                //send
                ((ServiceOnBack)getApplication()).sendMessage(message.getBytes());
                com.example.partytricks.Constant.setSodaState(com.example.partytricks.Constant.SodaState.SODA_SENDING);
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
    private void receiveMessage(String str){
        String message = str;
        if(message==null){
            Toast.makeText(SodaActivity.this, "null message accepted", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] splitMessage=message.split("/");
        int receive_current = Integer.parseInt(splitMessage[0]);
        int receive_full = Integer.parseInt(splitMessage[1]);
//            try {
//                Thread.sleep(5 * 10); //设置暂停的时间 5 秒
//                //current = i;
        count = receive_current;
//                tv4.setText("count" + count);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        current = receive_current;
        full = receive_full;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager am = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if(!(((ServiceOnBack)getApplication()).getSocket()==null)){
                    flagKeyDown = true;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(!(((ServiceOnBack)getApplication()).getSocket()==null)) {
                    flagKeyDown = true;
                }
                return true;
            default:
                break;
        }
        //return true;
        return super.onKeyDown(keyCode, event);
    }
    public void initBitmap(Resources r)
    {
        waters[0]=BitmapFactory.decodeResource(r, R.drawable.wp);//水0
        waters[1]=BitmapFactory.decodeResource(r, R.drawable.wpu);//水1 unvisible
        sodaBackground = BitmapFactory.decodeResource(r,R.drawable.soda_background);
        waterMask = BitmapFactory.decodeResource(r,R.drawable.water_mask);
    }
    public class SodaReceiver extends BroadcastReceiver{
        public static final String SODA_ACCEPT_RESPONSE = "action.PROCESS_RESPONSE";
        @Override
        public void onReceive(Context context, Intent intent){
            String responseString = intent.getStringExtra(SodaActivity.RESPONSE_STRING);
            if(responseString!=null){
                com.example.partytricks.Constant.setSodaState(com.example.partytricks.Constant.SodaState.SODA_ACCEPTING);
                receiveMessage(responseString);
            }
        }
    }
}
