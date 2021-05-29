package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.Direction;
import com.comandante.spacetrigger.Projectile;
import com.comandante.spacetrigger.SpriteSheetAnimation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public class AlienNymphBullet extends Projectile {
    public AlienNymphBullet(double x, double y) {
        super(x, y, Direction.DOWN, 12, 55);
    }

    @Override
    public void init() {
        loadImage(Assets.ALIEN_NYMPH_BULLET);
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienNymphBulletImpactExplosion(point);
    }
}
