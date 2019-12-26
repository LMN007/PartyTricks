package com.example.partytricks.box2d.software;

import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;
import android.graphics.Paint;

//自定义刚体根类
public abstract class MyBody 
{
	public Body body;//对应物理引擎中的刚体
	int color;//刚体的颜色
	int indext;//物体索引
	public abstract void drawSelf(Canvas canvas,Paint paint);
	public abstract void drawBitmap(Canvas canvas,GameView gv,Paint paint);
}
