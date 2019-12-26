package com.example.partytricks;

public class Constant {
    // 由Service中的Handler发送的消息类型
    public static final int MSG_READ = 2;
    public static final int MSG_DEVICE_NAME = 4;

    // 从Service中的Handler发来的主键名
    public static final String DEVICE_NAME = "device_name";

    public static final int REQUEST_CODE=1;//requestCode标识

    public static float accelerateX = 0.0f;
    public static float accelerateY = 10.0f;
    private static SodaState sodaState = SodaState.SODA_OUT;

    public static synchronized void setSodaState(Constant.SodaState state){
        sodaState = state;
    }
    public static Constant.SodaState getSodaState(){return sodaState;}

    public enum SodaState{
        SODA_ACCEPTING,
        SODA_SENDING,
        SODA_IN,
        SODA_OUT,
        SODA_BREAK,
    }
}
