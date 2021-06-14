package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.HealthPickUpEvent;
import com.comandante.spacetrigger.events.STEvent;

import java.util.Optional;

public class HealthDrop extends Drop {
    public HealthDrop(DropRate dropRate) {
        super(new PVector(0, 0), dropRate, Optional.of(Assets.HEALTH_DROP), Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Override
    public STEvent getEvent() {
        return new HealthPickUpEvent(30);
    }

    @Override
    public void update() {
        acceleration.add(new PVector(0, .3));
        velocity.add(acceleration);
        velocity.limit(.4);
        location.add(velocity);
        acceleration.mult(0);
    }
}
