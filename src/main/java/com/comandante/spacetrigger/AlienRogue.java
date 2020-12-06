package com.comandante.spacetrigger;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Optional;

public class AlienRogue extends AlienNymph {

    private int speed = 3;
    private boolean reverse;

    public AlienRogue(int x, int y) {
        super(x, y);
        loadWarpAnimation(new SpriteSheetAnimation(64, 64, 9, 1, Assets.RED_WARP, 0, 5));
    }

    public void fire() {
        missiles.add(new MachineGunMissle((x + width / 2) - 10, (y + height / 2) + 10, Direction.DOWN));
    }

    @Override
    public void move() {
        int stepSize = 100;
        if (ticks % stepSize == 0) {
            int randoPercent = random.nextInt(100);
            if (randoPercent > 80) {
                fire();
            }
        }
        for (int i = 0; i < speed; i++) {
            Point remove = trajectory.get(ticks);
            x = remove.getLocation().x;
            y = remove.getLocation().y + (int) Math.round(speed);
            if (reverse) {
                ticks--;
            } else {
                ticks++;
            }
            if (ticks == trajectory.size() - 1) {
                reverse = true;
            } else if (ticks == 0) {
                reverse = false;
            }
        }
    }
}
