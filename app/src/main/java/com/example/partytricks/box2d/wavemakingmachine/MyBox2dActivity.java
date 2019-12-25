package com.example.partytricks.box2d.wavemakingmachine;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;    
import org.jbox2d.dynamics.World;
import org.jbox2d.particle.ParticleSystem;
import org.jbox2d.particle.ParticleType;

import com.example.partytricks.R;
import com.example.partytricks.box2d.util.Box2DUtil;
import com.example.partytricks.box2d.util.Constant;
import com.example.partytricks.box2d.util.MyRevoluteJoint;
import com.example.partytricks.box2d.util.MyWeldJoint;
import com.example.partytricks.box2d.util.WaterObject;

import android.app.Activity;    
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;   
import android.util.DisplayMetrics;
import android.view.Window;   
import android.view.WindowManager;  
import static com.example.partytricks.box2d.util.Constant.*;
  
public class MyBox2dActivity extends Activity 
{  
    public World world;  
    public MyRevoluteJoint rj;//声明指向旋转关节
    public ParticleSystem m_particleSystem;//声明流体粒子系统
    //物体列表
    ArrayList<MyBody> bl=new ArrayList<MyBody>();
    public Bitmap water;//水图片
   
    public void onCreate(Bundle savedInstanceState) 
    {   
        super.onCreate(savedInstanceState);   
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);   
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,   
        WindowManager.LayoutParams. FLAG_FULLSCREEN); 
        //设置为横屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//获取屏幕尺寸
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        if(dm.widthPixels<dm.heightPixels)
        {
        	 SCREEN_WIDTH=dm.widthPixels;
             SCREEN_HEIGHT=dm.heightPixels;   
        }
        else
        {
        	SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;    
        }
        Constant.ScaleSR();//屏幕自适应
        
        initBitmap(this.getResources());//初始化图片
        Vec2 gravity = new Vec2(0.0f,10.0f);   
        //创建世界 
        world = new World(gravity); 
        
        //创建地面
        final int kd=20;//宽度或高度
        //创建包围框
        MyPolygonColor mrc=Box2DUtil.createBox//最底部
        (
        	0,
        	SCREEN_WIDTH-kd,
        	new float[][]
        	{
        	  {0,0},{SCREEN_HEIGHT,0},{SCREEN_HEIGHT,kd},{0,kd}
        	},
        	true,
        	world,
        	Color.YELLOW,
        	0,0,0,0
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最上面
	    (
    		0,
        	0,
        	new float[][]
        	{
        	  {0,0},{SCREEN_HEIGHT,0},{SCREEN_HEIGHT,kd},{0,kd}
        	},
        	true,
        	world,
        	Color.YELLOW,
        	1,0,0,0
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最右边
        (
        	SCREEN_HEIGHT-kd,
            0, 
            new float[][]
            {
        	  {0,0},{kd,0},{kd,SCREEN_WIDTH},{0,SCREEN_WIDTH}
            },
            true,
            world,
            Color.YELLOW,
            2,0,0,0
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最左边
        (
            0,
            0, 
            new float[][]
            {
              {0,0},{kd,0},{kd,SCREEN_WIDTH},{0,SCREEN_HEIGHT}
            },
            true,
            world,
            Color.YELLOW,
            3,0,0,0
        );
        bl.add(mrc);
        
        mrc=Box2DUtil.createBox//矩形木块  上部
        (
            (290+x)*ratio,
            (210+y)*ratio, 
            new float[][]
            {
              {0*ratio,0*ratio},{600*ratio,0*ratio},{600*ratio,6*ratio},{0*ratio,6*ratio}
            },
            false,
            world,
            Color.RED,
            4,0.1f,0.1f,0.0f
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//矩形木块  下部
        (
            (290+x)*ratio,
            (570+y)*ratio, //570
            new float[][]
            {
              {0*ratio,0*ratio},{600*ratio,0*ratio},{600*ratio,6*ratio},{0*ratio,6*ratio}
            },
            false,
            world,
            Color.RED,
            5,0.1f,0.1f,0.0f
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//矩形木块  左部
        (
            (290+x)*ratio,
            (210+y)*ratio, 
            new float[][]
            {
              {0*ratio,0*ratio},{6*ratio,0*ratio},{6*ratio,366*ratio},{0*ratio,366*ratio}
            },
            false,
            world,
            Color.RED,
            6,0.1f,0.1f,0.0f
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//矩形木块 右部
        (
            (884+x)*ratio,//884
            (210+y)*ratio, 
            new float[][]
            {
              {0*ratio,0*ratio},{6*ratio,0*ratio},{6*ratio,366*ratio},{0*ratio,366*ratio}
            },
            false,
            world,
            Color.RED,
            7,0.1f,0.1f,0.0f
        );
        bl.add(mrc); 
        
        new MyWeldJoint("W1",world,false,bl.get(4),bl.get(6),new Vec2((290+x)*ratio,(210+y)*ratio),0.0f,15,0.0f);//创建焊接关节对象
        new MyWeldJoint("W2",world,false,bl.get(5),bl.get(6),new Vec2((290+x)*ratio,(570+y)*ratio),0.0f,15,0.0f);//创建焊接关节对象
        new MyWeldJoint("W3",world,false,bl.get(4),bl.get(7),new Vec2((884+x)*ratio,(210+y)*ratio),0.0f,15,0.0f);//创建焊接关节对象
        new MyWeldJoint("W4",world,false,bl.get(5),bl.get(7),new Vec2((884+x)*ratio,(570+y)*ratio),0.0f,15,0.0f);//创建焊接关节对象
        rj=new MyRevoluteJoint("R1",world,false,bl.get(0),bl.get(5),new Vec2((590+x)*ratio,(573+y)*ratio),false,0,0,true,(float)(-0.042f*Math.PI),1e8f);//创建旋转关节对象

        m_particleSystem=world.m_particleSystem;
        m_particleSystem.setParticleRadius(0.28f);//设置粒子的半径
        m_particleSystem.setParticleDamping(0.2f);//设置粒子的潮湿因子
        WaterObject.createWaterRectObject((580+x)*ratio,(380+y)*ratio,200*ratio,150*ratio,1,ParticleType.b2_waterParticle,0,m_particleSystem);//创建水矩形对象
        
        GameView gv= new GameView(this);   
        setContentView(gv);   
    } 
    public void initBitmap(Resources r)
    {
    	water=BitmapFactory.decodeResource(r, R.drawable.wp);//水图片
    }
}  
