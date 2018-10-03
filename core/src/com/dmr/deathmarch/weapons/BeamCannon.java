package com.dmr.deathmarch.weapons;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class BeamCannon extends Rectangle {
    private int damage;
    private float cooldown;
    private float lastBeamShot;

    public BeamCannon(){
        damage = 25;
        cooldown = 500000000;
    }

    public int getDamage(){
        return damage;
    }

    public float getCooldown(){
        return cooldown;
    }

    public Rectangle shoot(Rectangle player){
        Rectangle laserBeam = new Rectangle();
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
