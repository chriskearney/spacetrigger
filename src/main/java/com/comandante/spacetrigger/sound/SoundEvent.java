package com.comandante.spacetrigger.sound;

public class SoundEvent {

    private final SoundEffectService.PlaySound playSound;

    public SoundEvent(SoundEffectService.PlaySound playSound) {
        this.playSound = playSound;
    }

    public SoundEffectService.PlaySound getPlaySound() {
        return playSound;
    }
}
