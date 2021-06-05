package com.comandante.spacetrigger.events;

import com.comandante.spacetrigger.PVector;

public class PlayerShipLocationUpdateEvent extends STEvent {

    private final PVector shipLocation;

    public PlayerShipLocationUpdateEvent(PVector shipLocation) {
        this.shipLocation = shipLocation;
    }

    public PVector getShipLocation() {
        return shipLocation;
    }
}
