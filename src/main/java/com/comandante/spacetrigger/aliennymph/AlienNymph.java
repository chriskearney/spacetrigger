package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.SpriteSheetAnimation;

public class AlienNymph extends Alien {

    public AlienNymph(int x, int y) {
        super(x, y, 160, 3);
        loadWarpAnimation(new SpriteSheetAnimation(64, 64, 9, 1, Assets.ALIEN_NYMPH_WARP, 0, 5));
    }

    public void fire() {
        projectiles.add(new AlienNymphBullet((x + width / 2) - 10, (y + height / 2)));
    }

    @Override
    protected void initAlien() {
        loadImage(Assets.ALIEN_NYMPH);
        loadExplosion(new SpriteSheetAnimation(128, 128, 8, 8, Assets.ALIEN_NYMPH_EXPLOSION, 2, 3));
    }
}
