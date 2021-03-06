package com.dmr.deathmarch;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.dmr.deathmarch.weapons.Weapon;

public class Player extends Sprite {
    private String name;
    private int health;
    private int speed;
    private int kills;
    private int dmgMulti;
    private Boolean isPlayerOne;
    private Texture playerTex;
    private Direction lastDirection[];
    private Weapon weapon;
    private boolean isDead;


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
    public void setName(String name){this.name = name;}

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
        if(health <= 0) return true;
        else return false;
    }
}