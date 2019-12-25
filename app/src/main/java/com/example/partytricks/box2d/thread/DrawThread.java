package com.example.partytricks.box2d.thread;

import static com.example.partytricks.box2d.util.Constant.*;

import com.example.partytricks.box2d.wavemakingmachine.GameView;
import com.example.partytricks.box2d.wavemakingmachine.MyPoint;

//绘制线程
public class DrawThread extends Thread 
{
	GameView gv;//GameView类的引用	
	
	public DrawThread(GameView gv)
	{
		this.gv=gv;//赋值
	}
	
	@Override
	public void run()//重写run方法
	{
		while(DRAW_THREAD_FLAG)
		{
			if(gv.point.length<=10)//创建一次后就不再创建
			{
				gv.point=new MyPoint[gv.b2ps1.length];//声明临时存储各个粒子的位置数组
				for(int i=0;i<gv.point.length;i++)//遍历数组
				{
					gv.point[i]=new MyPoint();//创建存储粒子位置的对象
				}
			}
			synchronized(gv.lock)//同步处理
			{
				if(gv.b2ps1.length>10&&gv.flag)//复制各个水粒子的位置
				{
					for(int i=0;i<gv.b2ps1.length;i++)//遍历所有水粒子的位置
					{
						gv.point[i].x=gv.b2ps1[i].x;//获取水粒子的x坐标
						gv.point[i].y=gv.b2ps1[i].y;//获取水粒子的y坐标
					}	
				}
			}
			gv.repaint();//刷帧绘制
			try 
			{
				Thread.sleep(17);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
