package com.example.partytricks.box2d.software;
import org.jbox2d.dynamics.Body;
import android.graphics.Canvas;
import android.graphics.Paint;
import static com.example.partytricks.box2d.util.Constant.*;

//自定义的圆形类
public class MyCircleColor extends MyBody
{
	float radius;//半径
	
	public MyCircleColor(Body body,float radius,int color,int indext)
	{
		this.body=body;//给刚体引用赋值
		this.radius=radius;//给圆形类物体半径变量赋值	
		this.color=color;//给颜色变量赋值
		this.indext=indext;
	}
	
	public void drawSelf(Canvas canvas,Paint paint)
	{ 		  
		paint.setColor(color&0x8CFFFFFF);//设置画笔颜色
		float x=body.getPosition().x*RATE;//获得现实世界里刚体的X坐标
		float y=body.getPosition().y*RATE;//获得现实世界里刚体的Y坐标		
		canvas.drawCircle(x, y, radius, paint);//画圆  
		paint.setStyle(Paint.Style.STROKE);//设置画笔类型
        paint.setStrokeWidth(1);//设置线条宽度
        paint.setColor(color);//设置画笔颜色
        canvas.drawCircle(x, y, radius, paint);//画圆 
		paint.reset();//重置画笔
	}
	@Override
	public void drawBitmap(Canvas canvas, GameView gv, Paint paint) {
		// TODO Auto-generated method stub
		
	}
}
