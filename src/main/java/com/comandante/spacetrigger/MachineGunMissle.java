package com.comandante.spacetrigger;

import java.awt.Point;
import java.util.Optional;

public class MachineGunMissle extends Missile {

    public MachineGunMissle(int x, int y, Direction direction) {
        super(x, y, direction, 12, 55);
    }

    @Override
    public void initMissile() {
        loadImage(Assets.MACHINEGUN_MISSLE);
    }

    public SpriteSheetAnimation getDamageAnimation(Point point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(32, 32, 8, 8, Assets.SMALL_MACHINEGUN_ANIMATION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }
}
