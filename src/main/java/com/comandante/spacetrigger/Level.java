package com.comandante.spacetrigger;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Level {

    final Map<Long, List<Alien>> alienTimeMap = new HashMap<>();

    public List<Alien> getAlien(long roundTimeElapsed) {
        List<Alien> readyToRenderAlien = Lists.newArrayList();
        List<Long> timeKeys = Lists.newArrayList();
        for (Map.Entry<Long, List<Alien>> alientTimeMapEntry : alienTimeMap.entrySet()) {
            if (roundTimeElapsed >= alientTimeMapEntry.getKey()) {
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
