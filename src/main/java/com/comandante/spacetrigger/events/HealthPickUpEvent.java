package com.comandante.spacetrigger.events;

import com.comandante.spacetrigger.Assets;

public class HealthPickUpEvent extends STEvent {

    private final int amt;

    public HealthPickUpEvent(int amt) {
        this.amt = amt;
    }

    public int getAmt() {
        return amt;
    }
}
