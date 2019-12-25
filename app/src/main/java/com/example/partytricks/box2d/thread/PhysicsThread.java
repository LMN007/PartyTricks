package com.example.partytricks.box2d.thread;

import static com.example.partytricks.box2d.util.Constant.DRAW_THREAD_FLAG;
import static com.example.partytricks.box2d.util.Constant.ITERA;
import static com.example.partytricks.box2d.util.Constant.TIME_STEP;

import com.example.partytricks.box2d.wavemakingmachine.GameView;

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
			if(gv.activity.rj.mJoint.getJointAngle()>(float)(Math.PI/24))
			{
				gv.activity.rj.mJoint.setMotorSpeed((float)(-0.042f*Math.PI));//改变旋转关节的角速度
			}else if(gv.activity.rj.mJoint.getJointAngle()<(float)(-Math.PI/24))
			{
				gv.activity.rj.mJoint.setMotorSpeed((float)(0.042f*Math.PI)); //改变旋转关节的角速度
			}
			synchronized(gv.lock)//缓存水粒子的各个点
			{
				gv.b2ps1=gv.activity.m_particleSystem.getParticlePositionBuffer();//获取粒子位置的缓冲区
			}
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
