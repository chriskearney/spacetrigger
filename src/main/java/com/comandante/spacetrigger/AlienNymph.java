package com.comandante.spacetrigger;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Optional;

public class AlienNymph extends Alien {

    public AlienNymph(int x, int y) {
        super(x, y, 160, 3);
        loadWarpAnimation(new SpriteSheetAnimation(64, 64, 9, 1, Assets.RED_WARP, 0, 5));
    }

    public void fire() {
        missiles.add(new MachineGunMissle((x + width / 2) - 10, (y + height / 2) + 10, Direction.DOWN));
    }

    @Override
    protected void initAlien() {
        loadImage(Assets.ALIEN_NYMPH);
        loadExplosion(new SpriteSheetAnimation(128, 128, 8, 8, Assets.ALIEN_NYMPH_EXPLOSION, 2, 3));
    }
}
