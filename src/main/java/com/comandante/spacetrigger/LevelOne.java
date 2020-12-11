package com.comandante.spacetrigger;

import com.comandante.spacetrigger.alienbuzz.AlienBuzz;
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
        alienNymph.pause(random.nextInt(4));
//        for (int j = 0; j < 1000; j++) {
//            alienNymph.addPoint(random.nextInt(500), random.nextInt(40, 800));
//        }
        alienNymph.pause(random.nextInt(4));
        alienNymph.addDrop(new MissleDrop(Drop.DropRate.RARE));
        eventBus.register(alienNymph);
        return alienNymph;
    }

    private AlienScout configureAlienScout(int pause, AlienScout alienScout) {
        alienScout.addDownAnglePath(random.nextInt(1, 3), .06, random.nextInt(400, 750), Sprite.DownAnglePathDirection.LEFT_TO_RIGHT);

        alienScout.pause(random.nextInt(4));
        alienScout.addDrop(new MissleDrop(Drop.DropRate.COMMON));
        eventBus.register(alienScout);
        return alienScout;
    }

    private AlienScout configureAlienScoutRight(int pause, AlienScout alienScout) {
        alienScout.addDownAnglePath(random.nextInt(5, 7), .06, random.nextInt(400, 750), Sprite.DownAnglePathDirection.LEFT_TO_RIGHT);
        alienScout.pause(random.nextInt(4));
        alienScout.addDrop(new MissleDrop(Drop.DropRate.COMMON));
        eventBus.register(alienScout);
        return alienScout;
    }

    public LevelOne(EventBus eventBus) {
        this.eventBus = eventBus;
        //1 - Second, Aka Start of the game:


//        for (int i = 0; i < 20; i++) {
//            alienTimeMap.put(4000L + (i * random.nextInt(200, 400)), Collections.singletonList(configureAlienBuzz(i, new AlienBuzz(random.nextInt(40, 80) * i, random.nextInt(50, 115)))));
//        }
//
//        for (int i = 0; i < 3; i++) {
//            alienTimeMap.put(6000L + (i * random.nextInt(200, 400)), Collections.singletonList(configureAlienScout(i, new AlienScout(random.nextInt(40, 80) * i, random.nextInt(50, 115)))));
//        }

        for (int i = 0; i < 5; i++) {
            alienTimeMap.put(4000L + (i * random.nextInt(40, 80)), Collections.singletonList(configureAlienRogueA(i, new AlienNymph(i * random.nextInt(40, 160), random.nextInt(100, 350)))));
        }

    }

    private AlienBuzz configureAlienBuzz(int i, AlienBuzz alienBuzz) {
        for (int j = 0; j < 1000; j++) {
            alienBuzz.addPoint(random.nextInt(500), random.nextInt(40, 800));
        }
        return alienBuzz;
    }
}
