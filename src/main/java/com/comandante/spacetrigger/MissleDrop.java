package com.comandante.spacetrigger;

import com.comandante.spacetrigger.events.MisslePickUpEvent;
import com.comandante.spacetrigger.events.STEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MissleDrop extends Drop {

    public MissleDrop(DropRate dropRate) {
        super(new PVector(), dropRate);
        initMissile();
    }

    public void initMissile() {
        loadImage(Assets.MISSLE_DROP);
    }

    @Override
    public STEvent getEvent() {
        return new MisslePickUpEvent(1);
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
