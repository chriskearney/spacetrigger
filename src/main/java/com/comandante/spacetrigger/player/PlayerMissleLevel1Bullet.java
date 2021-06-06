package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public class PlayerMissleLevel1Bullet extends Projectile {

    public PlayerMissleLevel1Bullet(double x, double y) {
        super(new PVector(x, y), new PVector(0, 0), 1000, Assets.PLAYER_MISSLE_LEVEL_1_BULLET);
        setVelocity(new PVector(0, -3));
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(64, 64, 8, 8, Assets.PLAYER_MISSLE_LEVEL_1_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        return spriteSheetAnimation;
    }

    @Override
    public void move() {
        location.add(velocity);
    }
}
