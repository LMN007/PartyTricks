package com.example.partytricks.box2d.software;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.particle.ParticleGroupType;
import org.jbox2d.particle.ParticleSystem;
import org.jbox2d.particle.ParticleType;

import com.example.partytricks.R;
import com.example.partytricks.box2d.util.Box2DUtil;
import com.example.partytricks.box2d.util.Constant;
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
import static com.example.partytricks.Constant.*;
  
public class MyBox2dActivity extends Activity 
{  
    public World world;
    public ParticleSystem m_particleSystem;//声明流体粒子系统
    //物体列表
    ArrayList<MyBody> bl=new ArrayList<MyBody>();
    public Bitmap[] waters=new Bitmap[2];
    public int watersIndex = 0;
    public Bitmap sodaBackground;
    public Bitmap waterMask;
   
    public void onCreate(Bundle savedInstanceState) 
    {   
        super.onCreate(savedInstanceState);   
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);   
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,   
        WindowManager.LayoutParams. FLAG_FULLSCREEN); 
        //设置为竖屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
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
        Vec2 gravity = new Vec2(accelerateX,accelerateY);
        //创建世界
        world = new World(gravity);
        
        //创建地面
        final int kd=1;//宽度或高度
        //创建包围框
        MyPolygonColor mrc=Box2DUtil.createBox//最底部
        (
        	0,
        	SCREEN_HEIGHT-kd,
        	new float[][]
        	{
        	  {0,0},{SCREEN_WIDTH,0},{SCREEN_WIDTH,kd},{0,kd}
        	},
        	true,
        	world,
        	Color.BLACK,
        	0,0,0,0
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最上面
	    (
    		0,
        	0,
        	new float[][]
        	{
        	  {0,0},{SCREEN_WIDTH,0},{SCREEN_WIDTH,kd},{0,kd}
        	},
        	true,
        	world,
        	Color.BLACK,
        	1,0,0,0
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最右边
        (
        	SCREEN_WIDTH-kd,
            0, 
            new float[][]
            {
        	  {0,0},{kd,0},{kd,SCREEN_HEIGHT},{0,SCREEN_HEIGHT}
            },
            true,
            world,
            Color.BLACK,
            2,0,0,0
        );
        bl.add(mrc);
        mrc=Box2DUtil.createBox//最左边
        (
            0,
            0, 
            new float[][]
            {
              {0,0},{kd,0},{kd,SCREEN_HEIGHT},{0,SCREEN_HEIGHT}
            },
            true,
            world,
            Color.BLACK,
            3,0,0,0
        );
        bl.add(mrc);

        m_particleSystem=world.m_particleSystem;
        m_particleSystem.setParticleRadius(1.6f);//设置粒子的半径
        m_particleSystem.setParticleDamping(0.4f);//设置粒子的潮湿因子
        m_particleSystem.setParticleGravityScale(10);

        WaterObject.createWaterRectObject((380+x)*ratio,(680+y)*ratio,500*ratio,500*ratio,2,ParticleType.b2_waterParticle,ParticleGroupType.b2_solidParticleGroup,m_particleSystem,0);//创建具有水属性的流体
        
        GameView gv= new GameView(this);   
        setContentView(gv);   
    } 
    public void initBitmap(Resources r)
    {
    	waters[0]=BitmapFactory.decodeResource(r, R.drawable.wp);//水0
        waters[1]=BitmapFactory.decodeResource(r, R.drawable.wpu);//水1 unvisible
        sodaBackground = BitmapFactory.decodeResource(r,R.drawable.soda_background);
        waterMask = BitmapFactory.decodeResource(r,R.drawable.water_mask);
    }
}  
