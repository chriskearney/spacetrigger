package com.comandante.spacetrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public abstract class Alien extends Sprite {

    protected int ticks = 0;
    protected final int originalX;
    protected final int originalY;
    protected List<Missile> missiles = new ArrayList<>();

    protected final SplittableRandom random = new SplittableRandom();

    public Alien(int x, int y, int hitPoints) {
        super(x, y, hitPoints);
        this.originalX = x;
        this.originalY = y;
        initAlien();
        visible = true;
    }

    protected abstract void initAlien();

    public List<Missile> getMissiles() {
        return missiles;
    }

}
