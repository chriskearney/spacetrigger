package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public class AlienNymphBullet extends Projectile {
    public AlienNymphBullet(double x, double y) {
        super(new PVector(x, y), new PVector(0, 0), 55, Assets.ALIEN_BUZZ_BULLET);
        setVelocity(new PVector(.1, .2));
        setAcceleration(new PVector(0, 0));
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienNymphBulletImpactExplosion(point);
    }

    @Override
    public void move() {
        super.move();
    }
}
