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
        for (int i = 0; i < 10; i++) {
            AlienBuzz alienBuzz = new AlienBuzz(new PVector(i * 40, 0));
            alienBuzz.addDrop(new HealthDrop(Drop.DropRate.COMMON));
            aliens.add(alienBuzz);

        }
        alienTimeMap.put(1000L, aliens);

        List<Alien> aliens2 = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            AlienBuzz alienBuzz = new AlienBuzz(new PVector(i * 40, 0));
            alienBuzz.addDrop(new HealthDrop(Drop.DropRate.COMMON));
            aliens2.add(alienBuzz);
        }
        alienTimeMap.put(5000L, aliens2);
    }


}
