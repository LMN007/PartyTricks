package com.example.partytricks.box2d.wavemakingmachine;
import org.jbox2d.common.Vec2;

import com.example.partytricks.box2d.thread.DrawThread;
import com.example.partytricks.box2d.thread.PhysicsThread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.example.partytricks.box2d.util.Constant.*;

public class GameView extends SurfaceView 
implements SurfaceHolder.Callback  //实现生命周期回调接口
{
	public MyBox2dActivity activity;
	Paint paint;//画笔		
	DrawThread dt;//绘制线程
	PhysicsThread pt;//物理模拟线程
	public Object lock=new Object();//锁
	public Vec2[] b2ps1=new Vec2[10];//存放各个水粒子的位置的缓存区
	public MyPoint[] point=new MyPoint[10];//存放各个水粒子的位置--绘制
	
	public GameView(MyBox2dActivity activity) 
	{
		super(activity);
		this.activity = activity;		
		//设置生命周期回调接口的实现者
		this.getHolder().addCallback(this);
		//初始化画笔
		paint = new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿
		//启动绘制线程
		dt=new DrawThread(this);
		pt=new PhysicsThread(this);
		dt.start();
		pt.start();
	} 
	public boolean flag=false;//判断是否在绘制的标志位

	public void onDraw(Canvas canvas)
	{		
		if(canvas==null)
		{
			return;
		}
		canvas.drawARGB(255, 255, 255, 255);
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 0,SCREEN_HEIGHT,SCREEN_WIDTH, paint);
		//绘制场景中的物体
		for(MyBody mb:activity.bl)
		{

			mb.drawSelf(canvas, paint);
		}
		flag=false;//正在绘制
		if(point.length>10)
		{
			 for(MyPoint v2:point)//循环遍历各个粒子的位置坐标
			 {
				 canvas.drawBitmap(activity.water, v2.x*RATE-5, v2.y*RATE-5, paint);//绘制图片
			 }
		}
		flag=true;//绘制结束
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) 
	{
		
	}

	public void surfaceCreated(SurfaceHolder holder) {//创建时被调用
		repaint();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//销毁时被调用

	}
	
	public void repaint()
	{
		SurfaceHolder holder=this.getHolder();
		Canvas canvas = holder.lockCanvas();//获取画布
		try{
			synchronized(holder){
				onDraw(canvas);//绘制
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}