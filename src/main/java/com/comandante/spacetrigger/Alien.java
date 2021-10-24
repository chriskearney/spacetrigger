package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.comandante.spacetrigger.Main.BOARD_Y;

public abstract class Alien extends Sprite {

    protected int ticks = 0;
    protected List<Projectile> projectiles = Lists.newArrayList();
    protected PVector shipLocation;

    protected List<Drop> drops = Lists.newArrayList();

    public Alien(EventBus eventBus,
                 PVector location,
                 int hitPoints,
                 Optional<BufferedImage> image,
                 Optional<SpriteSheetAnimation> imageAnimated,
                 Optional<SpriteSheetAnimation> explosionAnimation,
                 Optional<SpriteSheetAnimation> warpAnimation) {
        super(eventBus, location, hitPoints, image, imageAnimated, explosionAnimation, warpAnimation);
        visible = true;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public void addDrop(Drop drop) {
        this.drops.add(drop);
    }

    @Subscribe
    public void updateShipLocation(PlayerShipLocationUpdateEvent locationUpdateEvent) {
        shipLocation = locationUpdateEvent.getShipLocation();
    }

    protected double getMagnitudeOfVectorLengthToPlayerShip() {
        if (shipLocation != null) {
            PVector shipDirection = PVector.sub(shipLocation, location);
            return shipDirection.mag();
        }

        return 0;
    }

    protected Optional<PVector> getVectorToPlayerShip() {
        if (location == null || shipLocation == null) {
            return Optional.empty();
        }
        return Optional.of(PVector.sub(shipLocation, location));
    }

    protected boolean isShipInViewAngle(double coneDir, double coneAngle) {
        if (shipLocation == null) {
            return false;
        }
        return pointInViewAngle(shipLocation.x, shipLocation.y, coneDir, coneAngle);
    }

    @Override
    public void update(Optional<Double> rotateRadians) {
//        if ((location.y > BOARD_Y)) {
//            setVisible(false);
//            return;
//        }
        super.update(rotateRadians);
    }

    protected void applyFriction(double c) {
        PVector friction = velocity.get();
        friction.normalize();
        friction.mult(-1);
        friction.mult(c);
        applyForce(friction);
    }
}
