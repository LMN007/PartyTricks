package com.example.partytricks.box2d.util;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import com.example.partytricks.box2d.wavemakingmachine.MyBody;

import static com.example.partytricks.box2d.util.Constant.*;

public class MyRevoluteJoint//旋转关节类
{
	 public RevoluteJoint mJoint;//声明旋转关节对象
	 public World mWorld;//声明物理世界类对象
	 public MyRevoluteJoint(
		 String id,//关节id
		 World world,//物理世界对象
		 boolean collideConnected,//是否允许两个刚体碰撞
		 MyBody poA,//指向物体类对象A
		 MyBody poB,//指向物体类对象B
		 Vec2 anchor,//旋转锚点坐标
		 boolean enableLimit,//是否开启旋转限制
		 float lowerAngleScale,//底部角度系数
		 float upperAngleScale,//顶部角度系数
		 boolean enableMotor,//是否开启旋转马达
		 float motorSpeed,//马达速度
		 float maxMotorTorque//马达的最大力矩
	 )
	 {
		 this.mWorld=world;//给物理世界类对象赋值
		 RevoluteJointDef rjd=new RevoluteJointDef();//创建旋转关节描述
		 rjd.collideConnected=collideConnected;//给是否允许碰撞标志赋值
		 rjd.userData=id;//给关节描述的用户数据赋予关节id
		 rjd.enableLimit = enableLimit;//给是否开启旋转限制赋值
		 rjd.lowerAngle = (float) (lowerAngleScale *Math.PI);//给底部角赋值
		 rjd.upperAngle = (float) (upperAngleScale *Math.PI);//给顶部角赋值
		 rjd.enableMotor = enableMotor;//给是否开启旋转马达赋值
		 rjd.motorSpeed = motorSpeed;//给关节马达速度赋值
		 rjd.maxMotorTorque = maxMotorTorque;//给关节马达的最大扭矩赋值
		 anchor.x=anchor.x/RATE;//更改锚点的x坐标
		 anchor.y=anchor.y/RATE;//更改锚点的y坐标 
		 rjd.initialize(poA.body, poB.body, anchor);//调用旋转关节描述的初始化函数
		 mJoint=(RevoluteJoint)world.createJoint(rjd);//在物理世界里增添旋转关节

	 }

}
