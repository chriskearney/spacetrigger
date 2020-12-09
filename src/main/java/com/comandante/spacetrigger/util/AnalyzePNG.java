package com.comandante.spacetrigger.util;

import com.comandante.spacetrigger.Assets;
import com.comandante.spacetrigger.player.Shield;

import java.awt.image.BufferedImage;
import java.util.HashSet;

public class AnalyzePNG {

    public static void main(String[] args) {
        Shield shield = new Shield();
        HashSet<String> mask = shield.getMask(200);
        System.out.println(mask.size());
    }
}
