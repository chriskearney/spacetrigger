package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public class AlienNymphBullet extends Projectile {
    public AlienNymphBullet(double x, double y) {
        super(new PVector(x, y), Direction.DOWN, 12, 55);
    }

    @Override
    public void init() {
        loadImage(Assets.ALIEN_NYMPH_BULLET);
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienNymphBulletImpactExplosion(point);
    }
}
