package com.dmr.deathmarch.npc;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;


public class Goblin extends Sprite {
    private float health;
    private float speed;
    public Goblin(){
        health = 25;
        speed = 1;
    }
    public void takeDamage(float dmg){
        health-=dmg;
    }
    public Boolean isDead(){
        if(health==0){
            return true;
        }
        return false;
    }
    public float getHealth(){
        return health;
    }
}
