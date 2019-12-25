package com.example.partytricks.box2d.util;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.example.partytricks.box2d.wavemakingmachine.MyCircleColor;
import com.example.partytricks.box2d.wavemakingmachine.MyPolygonColor;

import static com.example.partytricks.box2d.util.Constant.*;

//生成物理形状的工具类
public class Box2DUtil 
{
	//创建矩形物体(颜色)
	public static MyPolygonColor createBox
	(
		float x,//x坐标
		float y,//y坐标
		float[][] points,//点序列
		boolean isStatic,//是否为静止的
		World world,//世界
		int color,//颜色
		int indext,//物体索引
        float density,//物体密度
        float friction,//物体摩擦系数
        float restitution//物体恢复系数
    )
	{   
		//创建刚体描述
		BodyDef bd=new BodyDef();
		//设置是否为可运动刚体
		if(isStatic)
		{
			bd.type=BodyType.STATIC;
		}   
		else
		{
			bd.type=BodyType.DYNAMIC;
		}
		//设置位置
		bd.position.set(x/RATE, y/RATE);
		
		//在世界中创建刚体
		Body bodyTemp= world.createBody(bd); 
		//创建刚体形状
		PolygonShape ps=new PolygonShape();
		Vec2[] vec=new Vec2[points.length];
		for(int i=0;i<vec.length;i++)
		{
			vec[i] =new Vec2(points[i][0]/RATE,points[i][1]/RATE);
		}
		ps.set(vec, vec.length);
		//创建刚体物理描述
		FixtureDef fd=new  FixtureDef();
		//设置密度
		fd.density =density; 
		//设置摩擦系数
		fd.friction =friction;   
		//设置能量损失率（反弹）
		fd.restitution =restitution;   
		//设置形状
		fd.shape=ps;
		//将刚体物理描述与刚体结合
		if(!isStatic)
		{
			bodyTemp.createFixture(fd);
		}
		else
		{
			bodyTemp.createFixture(ps, 0);
		}	
		return new MyPolygonColor(bodyTemp,color,indext,points);
	}   

	//创建圆形（颜色）
	public static MyCircleColor createCircle
	(
		float x,//x坐标
		float y,//y坐标
		float radius,//半径
		boolean isStatic,//是否为静止的
		World world,//世界
		int color,//颜色
		int indext,
		float density,//物体密度
        float friction,//物体摩擦系数
        float restitution//物体恢复系数
	)
	{   
		//创建刚体描述
		BodyDef bd=new BodyDef();
		//设置是否为可运动刚体
		if(isStatic)
		{
			bd.type=BodyType.STATIC;
		}   
		else
		{
			bd.type=BodyType.DYNAMIC;
		}
		//设置位置
		bd.position.set(x/RATE, y/RATE);
		//在世界中创建刚体
		Body bodyTemp= world.createBody(bd); 
		//创建刚体形状
		CircleShape cs=new CircleShape();
		cs.m_radius=radius/RATE;		
		//创建刚体物理描述
		FixtureDef fd=new  FixtureDef();	
		//设置密度
		fd.density =density;   		
		//设置摩擦系数
		fd.friction =friction;   
		//设置能量损失率（反弹）
		fd.restitution =restitution;  
		//设置形状
		fd.shape=cs;
		//将刚体物理描述与刚体结合
		bodyTemp.createFixture(fd);	
		
		return new MyCircleColor(bodyTemp,radius,color,indext);
	}   
}
