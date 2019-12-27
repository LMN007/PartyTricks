package com.example.partytricks.box2d.thread;

import android.graphics.BitmapFactory;

import static com.example.partytricks.box2d.util.Constant.*;
import static com.example.partytricks.Constant.*;

import com.example.partytricks.Constant;
import com.example.partytricks.R;
import com.example.partytricks.ServiceOnBack;
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
            Vec2[] particalVecBuff;
            Vec2[] particalPosBuff;
            gv.activity.world.setGravity(new Vec2(accelerateX,accelerateY+10));

		    switch(getSodaState()){
                case SODA_IN:
                    gv.activity.watersIndex=0;
                    //TODO test whether need to control velocity
                    particalVecBuff = gv.activity.m_particleSystem.getParticleVelocityBuffer();
                    for(int i=0; i<particalVecBuff.length;i++){
                        float vx=0.0f;
                        float vy=0.0f;
                        Vec2 vec = particalVecBuff[i];
                        if(Math.abs(vec.x+accelerateX*5)<100){
                            vx=vec.x+(-accelerateX*6);
                        }
                        if(Math.abs(vec.y+(accelerateY)*5)<100){
                            vy=vec.y+(accelerateY)*5;
                        }
                        particalVecBuff[i] = new Vec2(vx,vy);
                    }
                    gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
                    break;
                case SODA_OUT:
                    gv.activity.watersIndex=1;//set invisible
                    break;
                case SODA_BREAK://might be 1s-2s
                    gv.activity.watersIndex=0;
                    //TODO cancel simulation
//                    gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
                    break;
                case SODA_SENDING://might be 1s-2s
                    gv.activity.watersIndex=0;
                    boolean endsending = true;
                    //TODO
                    particalVecBuff = gv.activity.m_particleSystem.getParticleVelocityBuffer();
                    for(int i=0; i<particalVecBuff.length;i++){
                        particalVecBuff[i] = new Vec2(0,-100);
                    }
                    particalPosBuff = gv.activity.m_particleSystem.getParticlePositionBuffer();
                    for(int i=0;i<particalPosBuff.length;i++){
                        if(particalPosBuff[i].y>50){
                            endsending=false;
                            break;
                        }
                    }
                    if(endsending){
                        Constant.setSodaState(SodaState.SODA_OUT);
                    }
                    gv.activity.world.step(TIME_STEP, ITERA,ITERA);//开始模拟
                    break;
                case SODA_ACCEPTING://might be 1s-2s
                    gv.activity.watersIndex=0;
                    boolean endaccepting = true;
                    //TODO
                    particalVecBuff = gv.activity.m_particleSystem.getParticleVelocityBuffer();
                    for(int i=0; i<particalVecBuff.length;i++){
                        particalVecBuff[i] = new Vec2(0,100);
                    }
                    particalPosBuff = gv.activity.m_particleSystem.getParticlePositionBuffer();
                    for(int i=0;i<particalPosBuff.length;i++){
                        if(particalPosBuff[i].y<50){
                            endaccepting=false;
                            break;
                        }
                    }
                    if(endaccepting){
                        Constant.setSodaState(SodaState.SODA_IN);
                    }
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
