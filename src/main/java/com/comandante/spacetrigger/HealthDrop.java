package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.HealthPickUpEvent;
import com.comandante.spacetrigger.events.STEvent;
import com.google.common.eventbus.EventBus;

import java.util.Optional;

public class HealthDrop extends Drop {
    public HealthDrop(EventBus eventBus, DropRate dropRate) {
        super(eventBus, new PVector(0, 0), dropRate, Optional.of(Assets.HEALTH_DROP), Optional.empty(), Optional.empty(), Optional.empty());
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
