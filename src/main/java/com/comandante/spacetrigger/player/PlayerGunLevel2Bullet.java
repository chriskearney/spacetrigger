package com.comandante.spacetrigger.player;

import com.comandante.spacetrigger.*;
import com.google.common.eventbus.EventBus;

import java.awt.geom.Point2D;
import java.util.Optional;

public class PlayerGunLevel2Bullet extends Projectile {

    public PlayerGunLevel2Bullet(EventBus eventBus, double x, double y) {
        super(eventBus, new PVector(x, y), new PVector(0, 0), 10, Assets.PLAYER_GUN_LEVEL_2_BULLET, Assets.PLAYER_GUN_LEVEL_2_BULLET_SOUND);
        velocity.add(new PVector(0, -9));
    }

    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(32, 32, 8, 8, Assets.PLAYER_GUN_LEVEL_2_IMPACT_EXPLOSION, 2, 3, Optional.of(point));
        spriteSheetAnimation.setPlaySound(Assets.ALIEN_SCOUT_MISSLE_IMPACT_EXPLOSION_SOUND);
        return spriteSheetAnimation;
    }

    @Override
    public void update() {
        location.add(velocity);
    }
}
