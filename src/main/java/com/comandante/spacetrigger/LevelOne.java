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
    private final EventBus eventBus;


    public LevelOne(EventBus eventBus) {
        this.eventBus = eventBus;

        AlienBuzz aB = new AlienBuzz(0, 0);
        int alienBuzzWidth = aB.getWidth();


        AlienBuzz alienBuzz = new AlienBuzz(50, 100);
        eventBus.register(alienBuzz);
        alienBuzz.addPoint(300, 100);
        alienBuzz.addPoint(400, 100 + (50));

        AlienGroup alienGroup = new AlienGroup(AlienGroup.GroupOrientation.HORIZONTAL, 100, 100, eventBus);
        alienGroup.add(alienBuzz);
        alienGroup.add(new AlienBuzz(0, 0));
        alienGroup.add(new AlienBuzz(0, 0));
        alienGroup.add(new AlienBuzz(0, 0));

        alienTimeMap.put(1000L, Collections.singletonList(alienGroup));
    }
}
