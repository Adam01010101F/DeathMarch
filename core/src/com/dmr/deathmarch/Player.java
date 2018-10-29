package com.dmr.deathmarch;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.dmr.deathmarch.weapons.Weapon;
import com.sun.org.apache.xpath.internal.operations.Bool;

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
    private TiledMapTileLayer collisionLayer;
    private boolean isDead;

    public Player(String name, Boolean pone, Texture texture, int x, int y, int width, int height, TiledMapTileLayer collisionLayer){
        super(texture, x, y, width, height);
        this.name = name;
        health = 100;
        speed = 150;
        kills = 0;
        dmgMulti = 1;
        isPlayerOne = pone;
        lastDirection = new Direction[2];
        this.collisionLayer = collisionLayer;
    }


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

    public void checkGob(Sprite g) {
        if(this.getBoundingRectangle().overlaps(g.getBoundingRectangle()))
        {
            this.setX(this.getX() - 100);
            this.setY(this.getY() -100);
        }
    }

    public void setWeapon(Weapon weapon){this.weapon = weapon;}
    public Weapon getWeapon(){return weapon;}
    public Boolean isDead(){
        if(health == 0) return true;
        else return false;
    }
}