package com.comandante.spacetrigger.alienbuzz;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.util.Optional;

public class AlienBuzz extends Alien {

    public AlienBuzz(int x, int y) {
        super(x, y, 200, 1);
    }

    @Override
    protected void initAlien() {
        loadWarpAnimation(new SpriteSheetAnimation(64, 64, 9, 1, Assets.ALIEN_NYMPH_WARP, 0, 5));
        loadSpriteSheetAnimation(new SpriteSheetAnimation(32, 32, 6, 1, Assets.ALIEN_BUZZ, 0, 7, true, Optional.empty()));
        loadExplosion(new SpriteSheetAnimation(128, 128, 8, 8, Assets.ALIEN_NYMPH_EXPLOSION, 2, 3));
    }
}
