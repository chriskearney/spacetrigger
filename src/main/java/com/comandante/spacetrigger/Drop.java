package com.comandante.spacetrigger;


import com.comandante.spacetrigger.events.STEvent;

public abstract class Drop extends Sprite {

    private final DropRate dropRate;

    public Drop(PVector location, DropRate dropRate) {
        super(location);
        this.dropRate = dropRate;
    }

    public void move() {
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
        RARE(10),
        UNUSUAL(25),
        COMMON(40);

        private final int percent;

        DropRate(int percent) {
            this.percent = percent;
        }

        public int getPercent() {
            return percent;
        }
    }
}