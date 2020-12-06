package com.comandante.spacetrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public abstract class Alien extends Sprite {

    protected int ticks = 0;
    protected List<Missile> missiles = new ArrayList<>();

    public Alien(int x, int y, int hitPoints, int speed) {
        super(x, y, hitPoints, speed);
        initAlien();
        visible = true;
    }

    protected abstract void initAlien();

    public List<Missile> getMissiles() {
        return missiles;
    }

}
