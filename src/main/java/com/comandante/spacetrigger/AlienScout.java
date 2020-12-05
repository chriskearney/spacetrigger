package com.comandante.spacetrigger;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static java.lang.Math.sin;

public class AlienScout extends Alien {

    private double speed = 0.1;
    private final Direction direction;

    public AlienScout(Direction direction, int x, int y) {
        super(x, y, 1000);
        this.direction = direction;
    }

     protected void initAlien() {
        loadImage(Assets.ALIEN_SCOUT);
        loadExplosion(new SpriteSheetAnimation(188, 188, 8, 8, Assets.ALIEN_SCOUT_EXPLOSION, 2, 3));
    }

    public void fire() {
        missiles.add(new AlienMissle((x + width / 2) - 7, (y + height / 2) + 40));
    }

    //https://gamedevelopment.tutsplus.com/tutorials/quick-tip-create-smooth-enemy-movement-with-sinusoidal-motion--gamedev-6009
    public void move() {
        if (isExploding) {
            return;
        }

        int stepSize = 100;
        if (ticks % stepSize == 0) {
            int randoPercent = random.nextInt(100);
            if (randoPercent < 16) {
                fire();
            }
        }

        ticks++;
        //Controls whether you go left to right, or other way around.
        int xFactor = 4;
        if (direction.equals(Direction.RIGHT_TO_LEFT)) {
            xFactor = -xFactor;
        }
        int proposedX = (int) (BOARD_X / 3  * sin(ticks * .5 * Math.PI / (BOARD_X * xFactor))) + originalX;
        speed = speed + .3;
        int proposedY = (int) Math.round(speed);
        x = proposedX;
        y = proposedY + originalY;
    }

    enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }
}
