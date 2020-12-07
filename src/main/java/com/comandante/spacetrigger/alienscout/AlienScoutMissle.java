package com.comandante.spacetrigger.alienscout;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Direction;
import com.comandante.spacetrigger.Projectile;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.*;
import java.util.Optional;

public class AlienScoutMissle extends Projectile {

    public AlienScoutMissle(int x, int y) {
        super(x, y, Direction.DOWN, 4, 250);
    }

    @Override
    public void initMissile() {
        loadImage(Assets.ALIEN_SCOUT_MISSLE);
    }

    @Override
    public SpriteSheetAnimation getDamageAnimation(Point point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(64, 64, 8, 8, Assets.ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }

}