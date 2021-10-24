package com.comandante.spacetrigger.alienscout;

import com.comandante.spacetrigger.*;
import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class AlienScoutMissle extends Projectile {


    private PVector shipLocation;

    public AlienScoutMissle(EventBus eventBus, double x, double y, PVector heading) {
        super(eventBus, new PVector(x, y), heading, 250, Assets.ALIEN_SCOUT_MISSLE);
        this.shipLocation = heading;
        this.explosion = Assets.getAlienScoutMissleExplosion();
    }

    @Override
    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienScoutMissleImpactExplosion(point);
    }

    @Override
    public void update() {
        if (isOlderThan(3, TimeUnit.SECONDS) && !isExploding()) {
            setExploding(true, true);
        } else if (!isExploding()) {
            PVector sub = PVector.sub(shipLocation, location);
            sub.normalize();
            sub.mult(0.1);
            acceleration.add(sub);
            velocity.add(acceleration);
            velocity.limit(2);
            location.add(velocity);
            image = cachedRotate(Assets.ALIEN_SCOUT_MISSLE, GfxUtil.round(velocity.heading(), 1));
            acceleration.mult(0);

            if ((location.x > BOARD_X) || (location.x < 0)) {
                setVisible(false);
            }
            if ((location.y > BOARD_Y) || (location.y < 0)) {
                setVisible(false);
            }
        }
        super.update();
    }

    @Subscribe
    public void updateShipLocation(PlayerShipLocationUpdateEvent locationUpdateEvent) {
        shipLocation = locationUpdateEvent.getShipLocation();
    }
}
