package com.comandante.spacetrigger.util;

import com.comandante.spacetrigger.Assets;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CombineSpritesIntoSheet {

    public static void main(String[] args) throws IOException {


        List<BufferedImage> frameFiles = Lists.newArrayList();

        for (int i = 1; i <= 8; i++) {
            BufferedImage image = Assets.loadImage("engine_exhaust/engine_exhaust_" + i + ".png");
            frameFiles.add(image);
        }


        int width = 8;
        int heigth = 15;

        BufferedImage destinationImage = new BufferedImage(width * 8, heigth, frameFiles.get(0).getType());
        Graphics2D graphics = destinationImage.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        for (int i = 0; i < 8; i++) {
            graphics.drawImage(frameFiles.get(i), i * width, 0, null);
        }

        File outputfile = new File("player-ship-exhaust.png");
        ImageIO.write(destinationImage, "png", outputfile);
    }

}
