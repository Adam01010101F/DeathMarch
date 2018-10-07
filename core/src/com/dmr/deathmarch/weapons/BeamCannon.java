package com.dmr.deathmarch.weapons;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.dmr.deathmarch.Direction;
import com.dmr.deathmarch.Projectile;

public class BeamCannon extends Rectangle {
    private int damage;
    private float cooldown;
    private float lastBeamShot;
//    private Direction
    public BeamCannon(){
        damage = 25;
        cooldown = 100000000;
    }

    public int getDamage(){
        return damage;
    }

    public float getCooldown(){
        return cooldown;
    }

    public Projectile shoot(Rectangle player, Direction dir[]){
        Projectile laserBeam = new Projectile(dir[0], dir[1]);
        laserBeam.x = player.x;
        laserBeam.y = player.y;
        laserBeam.height = 32;
        laserBeam.width = 64;
        lastBeamShot = TimeUtils.nanoTime();
        return laserBeam;
    }

    public float getLastBeamShot(){
        return lastBeamShot;
    }
    public void setLastBeamShot(float time){
        lastBeamShot = time;
    }
}
