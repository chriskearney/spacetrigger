package com.comandante.spacetrigger.events;

public class MisslePickUpEvent extends STEvent {

    private final int amount;

    public MisslePickUpEvent(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
