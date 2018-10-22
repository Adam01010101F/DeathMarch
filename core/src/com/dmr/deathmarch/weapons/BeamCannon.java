package com.dmr.deathmarch.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.dmr.deathmarch.Direction;
import com.dmr.deathmarch.Player;
import com.dmr.deathmarch.Projectile;

import java.io.Serializable;

public class BeamCannon extends Weapon {
    private int damage;
    private float cooldown;
    private float lastBeamShot;

    public BeamCannon(Texture tex){
        super("Beam Cannon", tex, 25, 0, 300000000);
        damage = 25;
        cooldown = 300000000;
    }
}