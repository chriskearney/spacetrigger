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

        List<Alien> aliens = Lists.newArrayList();
        int firstX = 0 - ((alienBuzzWidth + 8) * 5);
        for (int i = 1; i < 6; i++) {
            int alienX = 0 - ((alienBuzzWidth + 8) * i);
            int alienY = 100;
            AlienBuzz alienBuzz = new AlienBuzz(alienX, alienY);
            eventBus.register(alienBuzz);
            alienBuzz.addPoint(alienX + 300, 100);
            aliens.add(alienBuzz);
        }
        alienTimeMap.put(1000L, aliens);
    }


}
