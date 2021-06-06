package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.HealthPickUpEvent;
import com.comandante.spacetrigger.events.STEvent;

public class HealthDrop extends Drop {
    public HealthDrop(DropRate dropRate) {
        super(new PVector(0, 0), dropRate);
        init();
    }

    public void init() {
        loadImage(Assets.HEALTH_DROP);
    }

    @Override
    public STEvent getEvent() {
        return new HealthPickUpEvent(30);
    }

    @Override
    public void move() {
        acceleration.add(new PVector(0, 2));
        velocity.add(acceleration);
        velocity.limit(2);
        location.add(velocity);
        acceleration.mult(0);
    }
}
