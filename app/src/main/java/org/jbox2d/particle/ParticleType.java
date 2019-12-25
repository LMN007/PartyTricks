package org.jbox2d.particle;

/**
 * The particle type. Can be combined with | operator. Zero means liquid.
 * 
 * @author dmurph
 */
public class ParticleType {//已写
  public static final int b2_waterParticle = 0;//表示具有水属性的粒子
  /** removed after next step */
  public static final int b2_zombieParticle = 1 << 1;//表示具有僵尸属性的粒子
  /** zero velocity */
  public static final int b2_wallParticle = 1 << 2;//表示具有壁属性的粒子
  /** with restitution from stretching */
  public static final int b2_springParticle = 1 << 3;//表示具有弹簧属性的粒子。
  /** with restitution from deformation */
  public static final int b2_elasticParticle = 1 << 4;//表示具有弹性、易伸缩属性的粒子。
  /** with viscosity */
  public static final int b2_viscousParticle = 1 << 5;
  /** without isotropic pressure */
  public static final int b2_powderParticle = 1 << 6;//表示具有面粉属性的粒子。
  /** with surface tension */
  public static final int b2_tensileParticle = 1 << 7;
  /** mixing color between contacting particles */
  public static final int b2_colorMixingParticle = 1 << 8;
  /** call b2DestructionListener on destruction */
  public static final int b2_destructionListener = 1 << 9;
}
