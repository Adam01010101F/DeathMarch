package com.dmr.deathmarch;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dmr.deathmarch.weapons.Weapon;

public class Player extends Sprite {
    private String name;
    private float health;
    private int speed;
    private int kills;
    private float dmgMulti;
    private Boolean isPlayerOne;
    private Texture playerTex;
    private Direction lastDirection[];
    private Weapon weapon;

    public Player(String name, Boolean pone, Texture texture){
        super(texture);
        this.name = name;
        health = 100;
        speed = 150;
        kills = 0;
        dmgMulti = 1;
        isPlayerOne = pone;
        lastDirection = new Direction[2];
    }
    public Player(String name, Boolean pone, Texture texture, int width, int height){
        super(texture, width, height);
        this.name = name;
        health = 100;
        speed = 150;
        kills = 0;
        dmgMulti = 1;
        isPlayerOne = pone;
        lastDirection = new Direction[2];
    }
    public Player(String name, Boolean pone, Texture texture, int x, int y, int width, int height){
        super(texture, x, y, width, height);
        this.name = name;
        health = 100;
        speed = 150;
        kills = 0;
        dmgMulti = 1;
        isPlayerOne = pone;
        lastDirection = new Direction[2];
    }

    public String getName() {
        return name;
    }
    public float getHealth(){
        return health;
    }
    public int getSpeed(){
        return speed;
    }
    public Direction[] getDirection(){return lastDirection;}
    public float getDmgMulti() {
        return dmgMulti;
    }
    public int getKills(){return kills;}

    public void takeDmg(float dmg){this.health -= dmg;}
    public void addKill(){kills++;}
    public void addHealth(float hp){health = health + hp;}
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
}
