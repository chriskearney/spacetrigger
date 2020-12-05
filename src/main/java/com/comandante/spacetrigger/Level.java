package com.comandante.spacetrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Level {

    final Map<Long, List<Alien>> alienTimeMap = new HashMap<>();

    public List<Alien> getAlien(long roundTimeElapsed) {
        List<Alien> readyToRenderAlien = new ArrayList<>();
        List<Long> timeKeys = new ArrayList<>();
        for (Map.Entry<Long, List<Alien>> alientTimeMapEntry : alienTimeMap.entrySet()) {
            if (roundTimeElapsed >= alientTimeMapEntry.getKey() * 1000) {
                readyToRenderAlien.addAll(alientTimeMapEntry.getValue());
                timeKeys.add(alientTimeMapEntry.getKey());
            }
        }
        for (int i = 0; i < timeKeys.size(); i++) {
            alienTimeMap.remove(timeKeys.get(i));
        }
        return readyToRenderAlien;
    }

    public boolean isEmpty() {
        return alienTimeMap.isEmpty();
    }

}
