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
        return Assets.getPlayerGunLevel2BulletDamageAnimation(point);
    }

    @Override
    public void update() {
        location.add(velocity);
    }
}
