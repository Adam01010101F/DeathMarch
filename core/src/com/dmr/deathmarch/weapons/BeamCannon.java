package com.dmr.deathmarch.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.dmr.deathmarch.Direction;
import com.dmr.deathmarch.Projectile;

import java.io.Serializable;

public class BeamCannon extends Rectangle {
    private int damage;
    private float cooldown;
    private float lastBeamShot;
//    private Texture laserBeamTex;
//    private Direction
    public BeamCannon(){
        damage = 25;
        cooldown = 300000000;
//        laserBeamTex = new Texture(Gdx.files.internal("laserBeam.png"));
    }

    public int getDamage(){
        return damage;
    }

    public float getCooldown(){
        return cooldown;
    }

    //TODO:: Make Rotation logic less ugly? Does it impact perf?
    public Projectile shoot(Rectangle player,Texture lbTex, Direction dir[]){
        Projectile laserBeam = new Projectile(lbTex, dir[0], dir[1]);
        laserBeam.setPosition(player.x, player.y);
        if(laserBeam.getxVel()==0&&laserBeam.getyVel()==1
                ||laserBeam.getxVel()==0&&laserBeam.getyVel()==-1)
            laserBeam.rotate(90);
        else if(laserBeam.getxVel() == -1&&laserBeam.getyVel() ==1
                ||laserBeam.getxVel()==1&&laserBeam.getyVel() ==-1) {
            laserBeam.rotate(135);
        }
        else if(laserBeam.getxVel()==1 && laserBeam.getyVel()==1
                ||laserBeam.getxVel()==-1 &&laserBeam.getyVel()==-1){
            laserBeam.rotate(45);
        }
        else {
            laserBeam.rotate(0);
        }
        lastBeamShot = TimeUtils.nanoTime();
        return laserBeam;
    }

//    public Sprite spShoot(Rectangle player, Direction dir[]){
//        Sprite lbeam = new Sprite(laserBeamTex);
//        lbeam.setPosition(player.x, player.y);
//        lbeam.setRotation(dir );
//    }

    public float getLastBeamShot(){
        return lastBeamShot;
    }
    public void setLastBeamShot(float time){
        lastBeamShot = time;
    }
}
