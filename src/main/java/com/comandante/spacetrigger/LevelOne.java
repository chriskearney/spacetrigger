package com.comandante.spacetrigger;

import com.comandante.spacetrigger.alienbuzz.AlienBuzz;
import com.comandante.spacetrigger.aliennymph.AlienNymph;
import com.comandante.spacetrigger.alienscout.AlienScout;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.util.*;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class LevelOne extends Level {

    private final SplittableRandom random = new SplittableRandom();

    public LevelOne(EventBus eventBus) {
        super(eventBus);
        List<Alien> aliens = Lists.newArrayList();
        for (int i = 0; i < 40; i++) {
//            PVector.random2D();
//            AlienBuzz alienBuzz = new AlienBuzz(new PVector(i * 40, 40));
//            alienBuzz.addDrop(new HealthDrop(Drop.DropRate.UNUSUAL));
//            alienBuzz.addDrop(new MissleDrop(Drop.DropRate.UNUSUAL));
//            aliens.add(alienBuzz);

        }
        alienTimeMap.put(1000L, aliens);

        List<Alien> aliens2 = Lists.newArrayList();
//        aliens2.add(new AlienScout(new PVector(200, 100), eventBus));w
//        aliens2.add(new AlienScout(new PVector(40, 120), eventBus));
//
//        aliens2.add(new AlienNymph(new PVector(20, 100)));
//        aliens2.add(new AlienNymph(new PVector(50, 100)));


        alienTimeMap.put(3500L, aliens2);
        List<Alien> aliens3 = Lists.newArrayList();

        for (int i = 0; i < 5; i++) {
            aliens3.add(new AlienScout(eventBus, new PVector(i * 60, 250)));
        }

//        aliens3.add(new AlienNymph(new PVector(20, 100)));
//        aliens3.add(new AlienNymph(new PVector(50, 100)));

        alienTimeMap.put(4000L, aliens3);


        List<Alien> aliens4 = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            PVector.random2D();
            AlienBuzz alienBuzz = new AlienBuzz(eventBus, new PVector(i * 40, 40));
            alienBuzz.addDrop(new HealthDrop(eventBus, Drop.DropRate.COMMON));
            alienBuzz.addDrop(new MissleDrop(eventBus, Drop.DropRate.COMMON));
            aliens4.add(alienBuzz);
        }
        alienTimeMap.put(900L, aliens4);

    }


}
