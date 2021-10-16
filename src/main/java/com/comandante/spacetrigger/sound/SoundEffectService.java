package com.comandante.spacetrigger.sound;

import com.google.common.eventbus.Subscribe;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundEffectService {

    private final ExecutorService soundService = Executors.newFixedThreadPool(24);

    @Subscribe
    public void listenSoundEvent(SoundEvent event) {
        soundService.submit(event.getPlaySound());
    }

    public static class PlaySound implements Runnable {

        private final byte[] sound;
        private final int numOfLoops;

        public PlaySound(byte[] sound, int numOfLoops) {
            this.sound = sound;
            this.numOfLoops = numOfLoops;
        }

        @Override
        public void run() {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(sound));
                clip.open(inputStream);
                if (numOfLoops > 0) {
                    clip.loop(numOfLoops);
                }
                clip.start();
                while (clip.isRunning()) {
                    Thread.sleep(1);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

