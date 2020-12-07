package com.comandante.spacetrigger.util;

import com.comandante.spacetrigger.Assets;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StackPngOnTopOfEachOther {

    public static void main(String[] args) throws IOException {

        BufferedImage bufferedImage = Assets.loadImage("bg_space_seamless_fl2.png");

        BufferedImage destinationImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight() * 2, bufferedImage.getType());
        Graphics2D graphics = destinationImage.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));

        graphics.drawImage(bufferedImage, 0, 0, null);
        graphics.drawImage(bufferedImage, 0, bufferedImage.getHeight(), null);

        File outputfile = new File("board-background-1.png");
        ImageIO.write(destinationImage, "png", outputfile);
    }


}
