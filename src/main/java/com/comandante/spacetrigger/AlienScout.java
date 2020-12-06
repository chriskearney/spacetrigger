package com.comandante.spacetrigger;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static java.lang.Math.sin;

public class AlienScout extends Alien {

    private double speed = 0.1;
    private int scoutTicks;

    public AlienScout(int x, int y) {
        super(x, y, 1000, 1);
    }

     protected void initAlien() {
        loadImage(Assets.ALIEN_SCOUT);
        loadExplosion(new SpriteSheetAnimation(188, 188, 8, 8, Assets.ALIEN_SCOUT_EXPLOSION, 2, 3));
        loadWarpAnimation(new SpriteSheetAnimation(188, 188, 9, 1, Assets.RED_WARP_ALIEN_SCOUT, 0, 3));
    }

    public void fire() {
        missiles.add(new AlienMissle((x + width / 2) - 7, (y + height / 2) + 40));
    }

    public void move() {
        super.move();
        int stepSize = 100;
        if (scoutTicks % stepSize == 0) {
            int randoPercent = random.nextInt(100);
            if (randoPercent < 16) {
                fire();
            }
        }
        scoutTicks++;
    }

    enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }
}
