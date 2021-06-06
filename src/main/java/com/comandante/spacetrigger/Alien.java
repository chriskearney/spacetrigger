package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.PlayerShipLocationUpdateEvent;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Alien extends Sprite {

    protected int ticks = 0;
    protected List<Projectile> projectiles = Lists.newArrayList();
    protected PVector shipLocation;

    protected List<Drop> drops = Lists.newArrayList();

    public Alien(PVector location, int hitPoints) {
        super(location, hitPoints);
        initAlien();
        visible = true;
    }

    protected abstract void initAlien();

    public List<Projectile> getMissiles() {
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
        if (location == null) {
            return Optional.empty();
        }
        return Optional.of(PVector.sub(shipLocation, location));
    }

    protected void applyFriction(double c) {
        PVector friction = velocity.get();
        friction.normalize();
        friction.mult(-1);
        friction.mult(c);
        applyForce(friction);
    }
}
