package com.comandante.spacetrigger.events;

public class PlayerShipShieldUpdateEvent {

    private final int newPct;

    public PlayerShipShieldUpdateEvent(int newPct) {
        this.newPct = newPct;
    }

    public int getNewPct() {
        return newPct;
    }
}
