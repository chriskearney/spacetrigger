package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Direction;
import com.comandante.spacetrigger.Projectile;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.*;
import java.util.Optional;

public class AlienNymphBullet extends Projectile {
    public AlienNymphBullet(int x, int y) {
        super(x, y, Direction.DOWN, 12, 55);
    }

    @Override
    public void initMissile() {
        loadImage(Assets.ALIEN_NYMPH_BULLET);
    }

    public SpriteSheetAnimation getDamageAnimation(Point point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(32, 32, 8, 8, Assets.ALIEN_NYMPH_BULLET_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }
}
