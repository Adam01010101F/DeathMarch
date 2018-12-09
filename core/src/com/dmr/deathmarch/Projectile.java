package com.dmr.deathmarch;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Projectile extends Sprite {
    private float xVel;
    private float yVel;
    private int bounceCount;

    public Projectile(){
        xVel = 0;
        yVel = 0;
        bounceCount = 0;
    }

    public Projectile(Texture tex, Direction x, Direction y){
        super(tex);
        if(x == Direction.None)
            this.xVel = 0;
        if(y == Direction.None)
            this.yVel = 0;
        if(y == Direction.Up) {
            this.yVel = 1;
        }
        else if(y == Direction.Down){
            this.yVel = -1;
        }
        if(x == Direction.Left){
            this.xVel = -1;
        }
        else if(x ==  Direction.Right){
            this.xVel = 1;
        }
    }

    public void incrementBounceCount(){ this.bounceCount++;}

    public int getBounceCount(){ return this.bounceCount; }

    public float getxVel() {
        return xVel;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    public float[] getVel(){
        float[] vel ={ xVel,yVel};
        return vel;
    }

    public void bounce(float xVel, float yVel,int y){
    float m = (float)(Math.random()*100+1);
    float n = (float)(Math.random()*100+1);

    switch(y){
        //left
        case 1:
            this.xVel = 1;
            if(n%2 == 0)
                this.yVel = 1;
            else
                this.yVel = -1;
            bounceCount++;
            break;
        //right
        case 2:
            this.xVel = -1;
            if(n%2 == 0)
                this.yVel = 1;
            else
                this.yVel = -1;
            bounceCount++;
            break;
        //south
        case 3:
            this.yVel = 1;
            if(m%2 == 0)
                this.xVel = 1;
            else
                this.xVel = -1;
            bounceCount++;

            break;
        //north
        case 4:
            this.yVel = -1;
            if(m%2 == 0)
                this.xVel = 1;
            else
                this.xVel = -1;
            bounceCount++;
            break;
    }

       /* //north
        if(yVel>= 1) {
            this.yVel = -1;
            if(m%2 == 0)
                this.xVel = 1;
            else
                this.xVel = -1;

            bounceCount++;
        }
        //south
        if(yVel<= -1){
            this.yVel = 1;
            if(m%2 == 0)
                this.xVel = 1;
            else
                this.xVel = -1;
            bounceCount++;
        }
        //east
        if(xVel>= 1){
            this.xVel = -1;
            if(n%2 == 0)
                this.yVel = 1;
            else
                this.yVel = -1;
            bounceCount++;
        }
        //west
        if(xVel <= -1){
            this.xVel = 1;
            if(n%2 == 0)
                this.yVel = 1;
            else
                this.yVel = -1;
            bounceCount++;
        }*/
    }


    public static void rebound(Projectile proj){
        proj.xVel *= -1;
        proj.yVel *=-1;
    }
}
