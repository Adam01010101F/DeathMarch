package com.dmr.deathmarch.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dmr.deathmarch.Direction;
import com.dmr.deathmarch.weapons.Weapon;

public class Bunny extends Sprite{
    private int health;
    private int speed;
    private int kills;
    private int dmgMulti;
    private Texture playerTex;
    private Direction lastDirection[];
    private Weapon weapon;
    private boolean isDead;
    private int moveCounter;

    public Bunny(Texture texture){
        super(texture);
        health = 100;
        speed = 150;
        kills = 0;
        dmgMulti = 1;
        lastDirection = new Direction[2];
        moveCounter = 0;
    }
    public Bunny(Texture texture, int width, int height){
        super(texture, width, height);
        health = 100;
        speed = 150;
        kills = 0;
        dmgMulti = 1;
        lastDirection = new Direction[2];
        moveCounter = 0;

    }
    public Bunny(Texture texture, int x, int y, int width, int height){
        super(texture, x, y, width, height);
        health = 100;
        speed = 150;
        kills = 0;
        dmgMulti = 1;
        lastDirection = new Direction[2];
        moveCounter = 0;

    }



    public float getHealth(){
        return health;
    }
    public int getSpeed(){
        return speed;
    }
    public void buffHealth(){
        this.health = 150;
    }
    public void buffSpeed(){
        this.speed = 175;
    }
    public void buffDmg(){
        this.dmgMulti = 2;
    }
    public Direction[] getDirection(){return lastDirection;}
    public int getDmgMulti() {
        return dmgMulti;
    }
    public int getKills(){return kills;}
    public void setDamage(int i){ i = i*2;}

    public void takeDmg(int dmg){this.health -= dmg;}
    public void setDmgMulti(int i){dmgMulti = i ;}
    public void addKill(){kills++;}

    public void addHealth(){health ++;}
    public void addBigKill(){kills = kills + 250;}
    public void resetKills(){kills = 0;}
    public void addHealth(int hp){health = health + hp;}
    public void resetHealth(){health = 100;}
    public void setHealth(int i){health = i;}
    public void setDirection(Direction[] directions){
        this.lastDirection = directions;
    }
    public Direction getXDirection(){return lastDirection[0];}
    public void setXDirection(Direction x){lastDirection[0]=x;}
    public Direction getYDirection(){return lastDirection[1];}
    public void setYDirection(Direction y){lastDirection[1]=y;}
    public Direction[] getLastDirection(){return lastDirection;}
    public void clearDirections(){
        lastDirection[0] = Direction.None;
        lastDirection[1] = Direction.None;
    }

    public void setWeapon(Weapon weapon){this.weapon = weapon;}
    public Weapon getWeapon(){return weapon;}
    public Boolean isDead(){
        if(health <= 0) return true;
        else return false;
    }

    public void incrCounter(){
        ++moveCounter;
    }
    public int getCounter(){
        return moveCounter;
    }


}
