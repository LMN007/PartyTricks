package com.example.partytricks.box2d.util;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.particle.ParticleColor;
import org.jbox2d.particle.ParticleGroupDef;
import org.jbox2d.particle.ParticleSystem;

import static com.example.partytricks.box2d.util.Constant.*;

public class WaterObject
{
	 public static void createWaterCycleObject//创建圆形的水
	 (
		 float x,
		 float y,
		 float radis,
		 float strength, 
		 int ptype,
		 int gtype,
		 ParticleSystem m_particleSystem,
		 int color
	 )
	 {
		 CircleShape shape = new CircleShape();//创建圆形
		 shape.m_p.set(x/RATE, y/RATE);//设置圆形的中心位置
		 shape.m_radius =radis/RATE;//设置圆形的半径
		 ParticleGroupDef pd = new ParticleGroupDef();//创建粒子群描述
		 pd.flags =ptype;//设置粒子群中粒子的类型
		 pd.groupFlags=gtype;//设置粒子群的类型
		 pd.strength=strength;//设置粒子的凝聚强度
		 pd.shape =shape;//设置粒子群的形状
		 pd.color=new ParticleColor((byte)color,(byte)color,(byte)color,(byte)color);
		 m_particleSystem.m_groupList=m_particleSystem.createParticleGroup(pd);//创建粒子群对象
	 }
	 public static void createWaterRectObject//创建矩形的水
	 (
		 float x,
		 float y,
		 float w,
		 float h,
		 float strength,
		 int ptype,
		 int gtype,
		 ParticleSystem m_particleSystem,
		 int color
	 )
	 {
		 PolygonShape shape = new PolygonShape();//创建多边形
		 shape.setAsBox(w/RATE, h/RATE);//设置多边形的半宽半高
		 ParticleGroupDef pd = new ParticleGroupDef();//创建粒子群描述
		 pd.position.set(x/RATE, y/RATE);//设置粒子群的位置
		 pd.flags =ptype;//设置粒子的类型
		 pd.groupFlags=gtype;//设置粒子群的类型
		 pd.strength=strength;//设置粒子的凝聚强度
		 pd.shape =shape;//设置粒子群的形状
		 pd.color=new ParticleColor((byte)color,(byte)color,(byte)color,(byte)color);
		 m_particleSystem.m_groupList=m_particleSystem.createParticleGroup(pd);//创建粒子群对象
	 }
}
