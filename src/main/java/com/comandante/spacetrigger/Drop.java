package com.comandante.spacetrigger;


import com.comandante.spacetrigger.events.STEvent;
import com.google.common.eventbus.EventBus;

import java.awt.image.BufferedImage;
import java.util.Optional;

public abstract class Drop extends Sprite {

    private final DropRate dropRate;

    public Drop(EventBus eventBus,
                PVector location,
                DropRate dropRate,
                Optional<BufferedImage> image,
                Optional<SpriteSheetAnimation> imageAnimation,
                Optional<SpriteSheetAnimation> explosionAnimation,
                Optional<SpriteSheetAnimation> warpAnimation) {
        super(eventBus, location, 0,
                image,
                imageAnimation,
                explosionAnimation,
                warpAnimation);
        this.dropRate = dropRate;
    }

    public void update() {
        location.y += Math.round(1);

        if (location.y > Main.BOARD_Y) {
            setVisible(false);
        }

        if (location.y < 0) {
            setVisible(false);
        }
    }

    public abstract STEvent getEvent();

    public DropRate getDropRate() {
        return dropRate;
    }

    public enum DropRate {
        RARE(2),
        UNUSUAL(6),
        COMMON(14);

        private final int percent;

        DropRate(int percent) {
            this.percent = percent;
        }

        public int getPercent() {
            return percent;
        }
    }
}