package com.comandante.spacetrigger;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RotateSpriteSheet {

    // Rotate the frames of a spritesheet.
    public static void main(String[] args) throws IOException {

        int imageWidth = 512;
        int imageHeight = 512;
        int columns = 8;
        int rows = 8;

        BufferedImage bufferedImage = Assets.loadImage("explosions/explosion 2.png");
        SpriteSheetAnimation spriteSheetAnimation = new SpriteSheetAnimation(imageWidth, imageHeight, columns, rows, bufferedImage, 0, 0);
        List<BufferedImage> spriteFrames = spriteSheetAnimation.getSpriteFrames();

        List<BufferedImage> rotatedImages = new ArrayList<>();

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));

        for (BufferedImage spriteFrame : spriteFrames) {
            rotatedImages.add(rotateNinetyDegrees(spriteFrame));
        }

        int currentImage = 0;
        graphics.clearRect(0, 0, imageWidth, imageHeight);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {
                graphics.drawImage(rotatedImages.get(currentImage), i * imageWidth, j * imageHeight, null);
                currentImage++;
            }
        }

        File outputfile = new File("vertical-explosion-2.png");
        ImageIO.write(bufferedImage, "png", outputfile);
    }

    public static BufferedImage rotateNinetyDegrees(BufferedImage image) {
        final double rads = Math.toRadians(90);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image,rotatedImage);
        return rotatedImage;
    }
}
