package com.example.partytricks.box2d.wavemakingmachine;
import org.jbox2d.dynamics.Body;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import static com.example.partytricks.box2d.util.Constant.*;

//自定义的矩形类(颜色)
public class MyPolygonColor extends MyBody
{	
	Path path;
	float[][] points;
	public MyPolygonColor(Body body,int color,int indext,float[][] points)
	{
		this.body=body;//给MyBody对象赋值
		this.color=color;//给Color对象赋值
		this.indext=indext;//给索引值赋值
		this.body.setUserData(this.indext);//设置数据
		path=new Path();//创建Path对象
		this.points=points;//给点序列赋值
	}
	
	public void drawSelf(Canvas canvas,Paint paint)
	{ 		
		paint.setColor(color&0x8CFFFFFF);//给画笔设置颜色
		float x=body.getPosition().x*RATE;//获取x坐标
		float y=body.getPosition().y*RATE;//获取y坐标
		float angle=body.getAngle();//获取角度
	    canvas.save();
	    Matrix m1=new Matrix();//创建Matrix对象
	    m1.setRotate((float)Math.toDegrees(angle),x, y);
	    canvas.setMatrix(m1);   
	    path.reset();//重置画笔
        path.moveTo(x+points[0][0], y+points[0][1]);
        for(int i=1;i<points.length;i++)//遍历点序列
        {
        	path.lineTo(x+points[i][0], y+points[i][1]);
        }
        path.lineTo(x+points[0][0], y+points[0][1]);
        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.STROKE);//设置为空心
        paint.setStrokeWidth(1);
        paint.setColor(color);//设置颜色
        canvas.drawPath(path, paint);
        paint.reset();//重置画笔
        canvas.restore();//保存画布
	}
	public void drawBitmap(Canvas canvas,GameView gv,Paint paint)
	{
		paint.setColor(color&0x8CFFFFFF);//设置画笔颜色
		float x=body.getPosition().x*RATE;//获得x坐标
		float y=body.getPosition().y*RATE;//获得y坐标
		float angle=body.getAngle();//获得角度
	    canvas.save();//保存画布
	    //创建旋转矩阵
	    Matrix m1=new Matrix();
	    m1.setRotate((float)Math.toDegrees(angle),x, y);
	    canvas.setMatrix(m1);
		canvas.drawBitmap(gv.activity.water,x,y,paint);//画图
	}
}
