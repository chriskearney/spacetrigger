package com.comandante.spacetrigger.events;

public class PlayerShipHealthUpdateEvent {

    private final int newPct;

    public PlayerShipHealthUpdateEvent(int newPct) {
        this.newPct = newPct;
    }

    public int getNewPct() {
        return newPct;
    }
}
