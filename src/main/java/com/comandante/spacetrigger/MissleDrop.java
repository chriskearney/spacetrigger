package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.MisslePickUpEvent;
import com.comandante.spacetrigger.events.STEvent;
import com.google.common.eventbus.EventBus;
import org.checkerframework.checker.nullness.Opt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class MissleDrop extends Drop {

    public MissleDrop(EventBus eventBus, DropRate dropRate) {
        super(eventBus, new PVector(), dropRate, Optional.of(Assets.MISSLE_DROP), Optional.empty(), Optional.empty(), Optional.empty());
    }


    @Override
    public STEvent getEvent() {
        return new MisslePickUpEvent(1);
    }

    @Override
    public void update() {
        acceleration.add(new PVector(0, 2));
        velocity.add(acceleration);
        velocity.limit(2);
        location.add(velocity);
        acceleration.mult(0);
    }
}
