package com.comandante.spacetrigger;

import com.comandante.spacetrigger.aliennymph.AlienNymph;
import com.comandante.spacetrigger.alienscout.AlienScout;
import com.google.common.eventbus.EventBus;

import java.util.*;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class LevelOne extends Level {

    private final SplittableRandom random = new SplittableRandom();
    private final EventBus eventBus;

    private AlienNymph configureAlienRogueA(int i, AlienNymph alienNymph) {
        alienNymph.pause(3);
        alienNymph.addPoint(350 + (i * 40), 400);
        alienNymph.pause(3);
        alienNymph.addDrop(new MissleDrop(Drop.DropRate.RARE));
        eventBus.register(alienNymph);
        return alienNymph;
    }

    private AlienScout configureAlienScout(int pause, AlienScout alienScout) {
        alienScout.addDownAnglePath(3, .06, 700, Sprite.DownAnglePathDirection.LEFT_TO_RIGHT);
        alienScout.pause(4);
        alienScout.addDrop(new MissleDrop(Drop.DropRate.COMMON));
        eventBus.register(alienScout);
        return alienScout;
    }

    private AlienScout configureAlienScoutRight(int pause, AlienScout alienScout) {
        alienScout.addDownAnglePath(7, .06, 700, Sprite.DownAnglePathDirection.RIGHT_TO_LEFT);
        alienScout.pause(4);
        alienScout.addDrop(new MissleDrop(Drop.DropRate.COMMON));
        eventBus.register(alienScout);
        return alienScout;
    }

    public LevelOne(EventBus eventBus) {
        this.eventBus = eventBus;
        //1 - Second, Aka Start of the game:


        for (int i = 0; i < 3; i++) {
            alienTimeMap.put(6000L + (i * 400), Collections.singletonList(configureAlienScout(i, new AlienScout(80 * i, 115))));
        }

        for (int i = 0; i < 5; i++) {
            alienTimeMap.put(4000L + (i * 200), Collections.singletonList(configureAlienRogueA(i, new AlienNymph(i * 40, 70))));
        }

        for (int i = 0; i < 5; i++) {
            alienTimeMap.put(12000L + (i * 200), Collections.singletonList(configureAlienRogueA(i, new AlienNymph(i * 40, 70))));
        }

        for (int i = 1; i < 4; i++) {
            alienTimeMap.put(19000L + (i * 400), Collections.singletonList(configureAlienScoutRight(i, new AlienScout(BOARD_X - (i * 80), 115))));
        }

        for (int i = 1; i < 6; i++) {
            alienTimeMap.put(20000L + (i * 200), Collections.singletonList(configureAlienRogueA(i, new AlienNymph(BOARD_X - (i * 40), 70))));
        }

        for (int i = 1; i < 4; i++) {
            alienTimeMap.put(30000L + (i * 400), Collections.singletonList(configureAlienScoutRight(i, new AlienScout(BOARD_X - (i * 80), 70))));
        }

        for (int i = 0; i < 5; i++) {
            alienTimeMap.put(31000L + (i * 200), Collections.singletonList(configureAlienRogueA(i, new AlienNymph(i * 40, 70))));
        }

        for (int i = 0; i < 5; i++) {
            alienTimeMap.put(33000L + (i * 200), Collections.singletonList(configureAlienRogueA(i, new AlienNymph(i * 88, 100))));
        }

    }
}
