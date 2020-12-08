package com.comandante.spacetrigger;


import com.comandante.spacetrigger.events.STEvent;

public abstract class Drop extends Sprite {

    private final DropRate dropRate;

    public Drop(int x, int y, int speed, DropRate dropRate) {
        super(x, y, speed);
        this.dropRate = dropRate;
    }

    public void move() {
        y += Math.round(speed);

        if (y > Main.BOARD_Y) {
            setVisible(false);
        }

        if (y < 0) {
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