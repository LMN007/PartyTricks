package com.example.partytricks.box2d.util;


public class Constant 
{
	public static final float RATE =20;//屏幕到现实世界的比例 10px：1m;   
	public static final boolean DRAW_THREAD_FLAG=true;//绘制线程工作标志位
	
	public static final float TIME_STEP = 2.0f/60.0f;//模拟的的频率   
	public static final int ITERA = 10;//迭代越大，模拟约精确，但性能越低   
	
	public static int SCREEN_WIDTH=720;  //屏幕宽度
	public static int SCREEN_HEIGHT=1280; //屏幕高度	
	
	public static float x;
    public static float y;
    public static float ratio;
    public static ScreenScaleResult screenScaleResult;
    public static void ScaleSR()
    {
    	screenScaleResult=ScreenScaleUtil.calScale(SCREEN_WIDTH, SCREEN_HEIGHT);
    	x=screenScaleResult.lucX;
    	y=screenScaleResult.lucY;
    	ratio=screenScaleResult.ratio;
    }
}
