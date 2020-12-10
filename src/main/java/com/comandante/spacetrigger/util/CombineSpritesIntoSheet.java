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

        int numberOfImages = 6;

        for (int i = 1; i <= numberOfImages; i++) {
            BufferedImage image = null;
                image = Assets.loadImage("SpaceShip_by_phobi/l0_SpaceShip001" + i + ".png");

            frameFiles.add(image);
        }


        int width = frameFiles.get(0).getWidth();
        int heigth = frameFiles.get(0).getHeight();

        BufferedImage destinationImage = new BufferedImage(width * numberOfImages, heigth, frameFiles.get(0).getType());
        Graphics2D graphics = destinationImage.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        for (int i = 0; i < numberOfImages; i++) {
            graphics.drawImage(frameFiles.get(i), i * width, 0, null);
        }

        File outputfile = new File("animated-ship.png");
        ImageIO.write(destinationImage, "png", outputfile);
    }

}
