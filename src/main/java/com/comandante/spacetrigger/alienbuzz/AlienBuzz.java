package com.comandante.spacetrigger.alienbuzz;

import com.comandante.spacetrigger.Alien;
import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.PVector;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.util.Optional;

public class AlienBuzz extends Alien {

    public AlienBuzz(PVector location) {
        super(location, 200, .03);
    }

    @Override
    protected void initAlien() {
        loadWarpAnimation(Assets.getAlienBuzzWarpAnimation());
        loadSpriteSheetAnimation(Assets.getAlienBuzzAnimation());
        loadExplosion(Assets.getAlienBuzzExplosion());
    }
}
