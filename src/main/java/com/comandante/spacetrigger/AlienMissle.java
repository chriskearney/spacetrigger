package com.comandante.spacetrigger;

public class AlienMissle extends Missile {

    public AlienMissle(int x, int y) {
        super(x, y, Direction.DOWN, 4, 250);
    }

    @Override
    public void initMissile() {
        loadImage(Assets.ALIEN_SCOUT_MISSLE);
    }

}
