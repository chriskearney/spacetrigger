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
        for (int i = 0; i < 5; i++) {
            PVector.random2D();
            AlienBuzz alienBuzz = new AlienBuzz(PVector.random2D());
            alienBuzz.addDrop(new HealthDrop(Drop.DropRate.UNUSUAL));
            aliens.add(alienBuzz);

        }
        alienTimeMap.put(1000L, aliens);

        List<Alien> aliens2 = Lists.newArrayList();
//        for (int i = 0; i < 15; i++) {
//            AlienBuzz alienBuzz = new AlienBuzz(new PVector(50, 60));
//            alienBuzz.addDrop(new HealthDrop(Drop.DropRate.COMMON));
//            aliens2.add(alienBuzz);
//        }

        aliens2.add(new AlienScout(new PVector(400, 200), eventBus));
        aliens2.add(new AlienScout(new PVector(200, 100), eventBus));
        aliens2.add(new AlienScout(new PVector(300, 200), eventBus));
        aliens2.add(new AlienScout(new PVector(40, 120), eventBus));


        alienTimeMap.put(1400L, aliens2);
    }


}
