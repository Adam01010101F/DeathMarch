package com.dmr.deathmarch.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.dmr.deathmarch.Direction;
import com.dmr.deathmarch.Player;
import com.dmr.deathmarch.Projectile;

public abstract class Weapon extends Rectangle {
    private String  name;
    private int damage;
    private float lastShot;
    private float cooldown;

    public Weapon(){
        name = "";
        damage = 0;
        lastShot = 0f;
        cooldown = 0f;
    }
    public Weapon(String name,Texture tex, int damage, float lastShot, float cooldown){
        this.name = name;
        this.damage = damage;
        this.lastShot = lastShot;
        this.cooldown = cooldown;
    }
    public int getDamage(){
        return damage;
    }

    public float getCooldown(){
        return cooldown;
    }

    //TODO:: Make Rotation logic less ugly? Does it impact perf?
    public Projectile shoot(Player player
            , Texture lbTex, Direction dir[]){
        Projectile projectile = new Projectile(lbTex, dir[0], dir[1]);
        projectile.setPosition(player.getBoundingRectangle().x , player.getBoundingRectangle().y);
        //Laser Direction Logic
        if(projectile.getxVel()==0&&projectile.getyVel()==1
                ||projectile.getxVel()==0&&projectile.getyVel()==-1)
            projectile.rotate(90);
        else if(projectile.getxVel() == -1&&projectile.getyVel() ==1
                ||projectile.getxVel()==1&&projectile.getyVel() ==-1) {
            projectile.rotate(135);
        }
//        else if(projectile.getxVel()==1 && projectile.getyVel()==1
//                ||projectile.getxVel()==-1 &&projectile.getyVel()==-1){
//            projectile.rotate(45);
//        }
        else {
            projectile.rotate(0);
        }
        //Set Shoot time;
        lastShot = TimeUtils.nanoTime();
        return projectile;
    }


    public float getLastShot(){
        return lastShot;
    }
    public void setLastShot(float time){
        lastShot = time;
    }
}

