package com.comandante.spacetrigger.alienscout;

import com.comandante.spacetrigger.*;
import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
import com.google.common.eventbus.Subscribe;

import java.awt.geom.Point2D;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class AlienScoutMissle extends Projectile {


    private PVector shipLocation;

    public AlienScoutMissle(double x, double y, PVector heading) {
        super(new PVector(x, y), heading, 250, Assets.ALIEN_SCOUT_MISSLE);
        this.shipLocation = heading;
    }

    @Override
    public SpriteSheetAnimation getDamageAnimation(Point2D point) {
        return Assets.getAlienScoutMissleImpactExplosion(point);
    }

    @Override
    public void update() {
        PVector sub = PVector.sub(shipLocation, location);
        sub.normalize();
        sub.mult(0.1);
        acceleration.add(sub);
//        acceleration.add(new PVector(0, .3));
        velocity.add(acceleration);
        velocity.limit(4);
        location.add(velocity);
         image = cachedRotate(Assets.ALIEN_SCOUT_MISSLE, GfxUtil.round(velocity.heading(), 1));
//        loadImage(GfxUtil.rotateImageByDegrees(Assets.ALIEN_SCOUT_MISSLE, velocity.heading()));
        acceleration.mult(0);

        if ((location.x > BOARD_X) || (location.x < 0)) {
            setVisible(false);
        }
        if ((location.y > BOARD_Y) || (location.y < 0)) {
            setVisible(false);
        }
        super.update();
    }

    @Subscribe
    public void updateShipLocation(PlayerShipLocationUpdateEvent locationUpdateEvent) {
        shipLocation = locationUpdateEvent.getShipLocation();
    }
}
