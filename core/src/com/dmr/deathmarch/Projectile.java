package com.dmr.deathmarch;

import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Projectile extends Rectangle{
    private float xVel;
    private float yVel;

    public Projectile(){
        xVel = 0;
        yVel = 0;
    }

    public Projectile(Direction x, Direction y){
        //Might need to add None Condition
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

}
