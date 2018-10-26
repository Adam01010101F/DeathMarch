package com.dmr.deathmarch;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Projectile extends Sprite {
    private float xVel;
    private float yVel;

    public Projectile(){
        xVel = 0;
        yVel = 0;
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
        float[] vel ={ xVel,   yVel};
        return vel;
    }

}
