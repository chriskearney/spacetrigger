package com.comandante.spacetrigger.sound;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.RateLimiter;
import tinysound.Sound;
import tinysound.TinySound;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundEffectService {

    private final ExecutorService soundService = Executors.newFixedThreadPool(1);

    public SoundEffectService() {
        TinySound.init();
    }

    @Subscribe
    public void listenSoundEvent(SoundEvent event) {
        soundService.submit(event.getPlaySound());
    }

    public static class PlaySound implements Runnable {

        private final Sound sound;
        private final int numOfLoops;
        private final RateLimiter rateLimiter = RateLimiter.create(5);

        public PlaySound(Sound sound, int numOfLoops) {
            this.sound = sound;
            this.numOfLoops = numOfLoops;
        }

        @Override
        public void run() {
            try {
                if (rateLimiter.tryAcquire()) {
                    sound.play();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

