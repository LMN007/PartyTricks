package com.example.partytricks.box2d.util;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

import com.example.partytricks.box2d.wavemakingmachine.MyBody;

import static com.example.partytricks.box2d.util.Constant.*;

public class MyWeldJoint //焊接关节类
{
	public WeldJoint mJoint;//声明焊接关节对象
	public World mWorld;//声明物理世界类对象的指针
	public MyWeldJoint(
		String id,//关节id
		World world,//物理世界对象
		boolean collideConnected,//是否允许两个刚体碰撞
		MyBody poA,//指向物体类对象A
		MyBody poB,//指向物体类对象B
		Vec2 anchor,//焊接关节的锚点
		float referenceAngle,//两个刚体之间的角度差
		float frequencyHz,//关节频率
		float dampingRatio//阻尼系数
	)
	{
		this.mWorld=world;//给物理世界类对象赋值
		WeldJointDef wjd = new WeldJointDef();//创建焊接关节描述
		wjd.collideConnected=collideConnected;//给是否允许碰撞标志赋值
		wjd.userData=id;//给关节描述的用户数据赋予关节id
		anchor.x=anchor.x/RATE;//将锚点的x坐标改为物理世界下的x坐标
		anchor.y=anchor.y/RATE;//将锚点的y坐标改为物理世界下的y坐标
		wjd.initialize(poA.body, poB.body, anchor);//调用焊接关节的初始化函数
		wjd.referenceAngle = referenceAngle;//设置刚体B与刚体A的角度差
		wjd.frequencyHz = frequencyHz;//给关节频率赋值
		wjd.dampingRatio = dampingRatio;//给阻尼系数赋值
		mJoint=(WeldJoint)world.createJoint(wjd);//在物理世界里增添这个关节
	}
}
