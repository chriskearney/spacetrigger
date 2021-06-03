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
            aliens.add(new AlienBuzz(new PVector(0, 0)));
        }
        alienTimeMap.put(1000L, aliens);
    }


}
