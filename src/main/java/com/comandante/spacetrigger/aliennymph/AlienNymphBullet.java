package com.comandante.spacetrigger.aliennymph;

import com.comandante.spacetrigger.*;
import com.google.common.eventbus.EventBus;

import java.awt.geom.Point2D;
import java.util.Optional;

public class AlienNymphBullet extends Projectile {
    public AlienNymphBullet(EventBus eventBus, double x, double y) {
        super(eventBus, new PVector(x, y), new PVector(0, 0), 55, Assets.ALIEN_NYMPH_BULLET);
        applyForce(new PVector(0, 4));
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienNymphBulletImpactExplosion(point);
    }

    @Override
    public void update(Optional<Double> rotateRadians) {
        velocity.add(acceleration);
        location.add(velocity);
        acceleration.mult(0);
        super.update(rotateRadians);
    }

    @Override
    public void update() {
        super.update();
    }
}
