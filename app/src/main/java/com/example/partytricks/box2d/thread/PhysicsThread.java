package com.example.partytricks.box2d.thread;

import static com.example.partytricks.box2d.util.Constant.*;

import com.example.partytricks.box2d.software.GameView;

public class PhysicsThread extends Thread
{
	GameView gv;//GameView类的引用	
	
	public PhysicsThread(GameView gv)
	{
		this.gv=gv;//赋值
	}
	
	@Override
	public void run()//重写run方法
	{
		while(DRAW_THREAD_FLAG)
		{
			gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
			synchronized(gv.lock)//缓存水粒子的各个点
			{
				gv.b2ps1=gv.activity.m_particleSystem.getParticlePositionBuffer();//获取粒子位置的缓冲区
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
