package com.example.partytricks.box2d.thread;

import android.graphics.BitmapFactory;

import static com.example.partytricks.box2d.util.Constant.*;
import static com.example.partytricks.Constant.*;

import com.example.partytricks.R;
import com.example.partytricks.box2d.software.GameView;

import org.jbox2d.common.Vec2;
import org.jbox2d.particle.ParticleColor;

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
            gv.activity.world.setGravity(new Vec2(accelerateX,accelerateY));

		    switch(getSodaState()){
                case SODA_IN:
                    //TODO test whether need to control velocity
                    Vec2[] particalVecBuff = gv.activity.m_particleSystem.getParticleVelocityBuffer();
                    for(int i=0; i<particalVecBuff.length;i++){
                        float vx=0.0f;
                        float vy=0.0f;
                        Vec2 vec = particalVecBuff[i];
                        if(Math.abs(vec.x+accelerateX*6)<100){
                            vx=vec.x+(-accelerateX*6);
                        }
                        if(Math.abs(vec.y+accelerateY*6)<100){
                            vy=vec.y+accelerateY*6;
                        }
                        particalVecBuff[i] = new Vec2(vx,vy);
                    }
                    gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
                    break;
                case SODA_OUT:
                    gv.activity.watersIndex=1;//set invisible
                    break;
                case SODA_BREAK://might be 1s-2s
                    //TODO
                    gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
                    break;
                case SODA_SENDING://might be 1s-2s
                    //TODO
                    gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
                    break;
                case SODA_ACCEPTING://might be 1s-2s
                    //TODO
                    gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
                    break;
                default:
                    break;
            }

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
