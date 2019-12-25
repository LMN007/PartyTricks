package org.jbox2d.particle;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

/**
 * A particle group definition holds all the data needed to construct a particle group. You can
 * safely re-use these definitions.
 */
public class ParticleGroupDef {

  /** The particle-behavior flags. */
  public int flags;//表示粒子的类型

  /** The group-construction flags. */
  public int groupFlags;//表示粒子群的类型

  /**
   * The world position of the group. Moves the group's shape a distance equal to the value of
   * position.
   */
  public final Vec2 position = new Vec2();//表示粒子群的中心位置

  /**
   * The world angle of the group in radians. Rotates the shape by an angle equal to the value of
   * angle.
   */
  public float angle;//表示粒子群的旋转角

  /** The linear velocity of the group's origin in world co-ordinates. */
  public final Vec2 linearVelocity = new Vec2();//表示粒子群的线速度

  /** The angular velocity of the group. */
  public float angularVelocity;//表示粒子群的角速度

  /** The color of all particles in the group. */
  public ParticleColor color;//表示粒子群中粒子的颜色信息

  /**
   * The strength of cohesion among the particles in a group with flag b2_elasticParticle or
   * b2_springParticle.
   */
  public float strength;//表示粒子群中粒子的凝聚强度

  /** Shape containing the particle group. */
  public Shape shape;//表示粒子群的形状

  /** If true, destroy the group automatically after its last particle has been destroyed. */
  public boolean destroyAutomatically;

  /** Use this to store application-specific group data. */
  public Object userData;//用来存储用户数据

  public ParticleGroupDef() {
    flags = 0;
    groupFlags = 0;
    angle = 0;
    angularVelocity = 0;
    strength = 1;
    destroyAutomatically = true;
  }
}
