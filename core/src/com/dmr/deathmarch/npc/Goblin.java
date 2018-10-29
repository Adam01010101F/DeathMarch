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

    public void checkGob(Sprite p) {
        float x = p.getX() - this.getX();
        float y = p.getY() - this.getY();
        if(p.getBoundingRectangle().overlaps(this.getBoundingRectangle())) {
            if (x > 0) {
                p.setX(p.getX() + 20);
            } else {
                p.setX(p.getX() - 20);
            }
            if (y > 0) {
                p.setY(p.getY() + 20);
            }
            else
            {
                p.setY(p.getY() - 20);
            }
        }
    }

    public void loop(){

    }
}
