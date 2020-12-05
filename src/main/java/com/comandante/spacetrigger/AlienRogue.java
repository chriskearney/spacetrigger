package com.comandante.spacetrigger;

import java.awt.Point;
import java.util.ArrayList;

public class AlienRogue extends AlienNymph {

    private int speed = 3;
    private boolean reverse;

    public AlienRogue(int x, int y) {
        super(x, y);
    }

    @Override
    public void move() {
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
