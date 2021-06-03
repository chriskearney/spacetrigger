package com.comandante.spacetrigger;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public abstract class Alien extends Sprite {

    protected int ticks = 0;
    protected List<Projectile> projectiles = Lists.newArrayList();

    protected List<Drop> drops = Lists.newArrayList();

    public Alien(PVector location, int hitPoints, double speed) {
        super(location, hitPoints, speed);
        initAlien();
        visible = true;
    }

    protected abstract void initAlien();

    public List<Projectile> getMissiles() {
        return projectiles;
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public void addDrop(Drop drop) {
        this.drops.add(drop);
    }
}
