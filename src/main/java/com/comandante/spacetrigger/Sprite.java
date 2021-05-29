package com.comandante.spacetrigger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;
import com.google.common.collect.Lists;


import static com.comandante.spacetrigger.Main.BOARD_X;
import static java.lang.Math.sin;

public abstract class Sprite {

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected int hitPoints = 0;
    protected int maxHitpoints;
    protected boolean visible;
    protected BufferedImage image;
    protected Optional<SpriteSheetAnimation> animatedImage = Optional.empty();
    protected SpriteSheetAnimation explosion;
    protected Optional<SpriteSheetAnimation> warpAnimation = Optional.empty();
    protected boolean isExploding;
    private boolean invisibleAfterExploding;
    protected int ticks = 0;
    protected int speed;
    protected boolean reverse = false;

    protected final SplittableRandom random = new SplittableRandom();

    protected List<SpriteSheetAnimation> damageAnimations = Lists.newArrayList();


    private double centerX;
    private double centerY;

    protected double originalX;
    protected double originalY;

    protected ArrayList<Point2D> trajectory = Lists.newArrayList();

    private Point2D previousAddedPoint;

    private static final BufferedImage TRANSPARENT_ONE_PIXEL = createTransparentBufferedImage(1, 1);

    public Sprite(double x, double y, int hitPoints, int speed) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.hitPoints = hitPoints;
        this.maxHitpoints = hitPoints;
        this.speed = speed;
        this.previousAddedPoint = new Point2D.Double(originalX, originalY);
        this.trajectory.add(previousAddedPoint);
        this.trajectory.add(previousAddedPoint);
    }

    public Sprite(double x, double y, int speed) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.speed = speed;
        this.previousAddedPoint = new Point2D.Double(originalX, originalY);
        this.trajectory.add(previousAddedPoint);
        this.trajectory.add(previousAddedPoint);
    }

    protected void loadImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    protected void loadSpriteSheetAnimation(SpriteSheetAnimation spriteSheetAnimation) {
        this.animatedImage = Optional.of(spriteSheetAnimation);
        this.width = spriteSheetAnimation.getX_size();
        this.height = spriteSheetAnimation.getX_size();
    }

    public void move() {
        if (isExploding || warpAnimation.isPresent()) {
            return;
        }
        for (int i = 0; i < speed; i++) {
            Point2D point = trajectory.get(ticks);
            x = point.getX();
            y = point.getY() + (int) Math.round(speed);
            if (reverse) {
                ticks--;
            } else {
                ticks++;
            }
            if (ticks == trajectory.size() - 1) {
                reverse = true;
            } else if (ticks == 0) {
                reverse = false;
            }
        }
    }

    protected void loadExplosion(SpriteSheetAnimation explosion) {
        this.explosion = explosion;
    }

    protected void loadWarpAnimation(SpriteSheetAnimation animation) {
        this.warpAnimation = Optional.of(animation);
    }

    public SpriteRender getSpriteRender() {

        if (!isExploding) {
            centerX = getX() + width / 2;
            centerY = getY() + height / 2;
        }

        if (warpAnimation.isPresent()) {
            Optional<BufferedImage> currentFrame = warpAnimation.get().updateAnimation();;
            if (currentFrame.isPresent()) {
                double warpX = centerX - (currentFrame.get().getWidth() / 2);
                double warpY = centerY - (currentFrame.get().getHeight() / 2);
                return new SpriteRender(warpX, warpY, currentFrame.get());
            } else {
                warpAnimation = Optional.empty();
            }
        }

        if (isExploding) {
            Optional<BufferedImage> currentFrame = explosion.updateAnimation();
            if (currentFrame.isPresent()) {
                double explosionX = centerX - (currentFrame.get().getWidth() / 2);
                double explosionY = centerY - (currentFrame.get().getHeight() / 2);
                return new SpriteRender(explosionX, explosionY, currentFrame.get());
            }

            if (invisibleAfterExploding) {
                setVisible(false);
                isExploding = false;
            }

            return new SpriteRender(x, y, TRANSPARENT_ONE_PIXEL);
        }

        if (animatedImage.isPresent()) {
            Optional<BufferedImage> bufferedImage = animatedImage.get().updateAnimation();
            if (!bufferedImage.isPresent()) {
                throw new RuntimeException("Need a looping animation.");
            }
            return new SpriteRender(x, y, bufferedImage.get());
        }

        return new SpriteRender(x, y, image);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setOriginalX(double x) {
        this.originalX = x;
        this.x = x;
    }

    public void setOriginalY(double y) {
        this.originalY = y;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public HashSet<String> getMask() {
        // can't have any transparency to make the msak in this case
        return getMask(254);
    }
    public HashSet<String> getMask(int threshold) {
        HashSet<String> mask = new HashSet<String>();
        int pixel;

        for (int i = 0; i < (getWidth()); i++) {
            for (int j = 0; j < getHeight(); j++) {
                BufferedImage maskImage = null;
                if (animatedImage.isPresent()) {
                    maskImage = animatedImage.get().getCurrentFrame().get();
                } else {
                    maskImage = image;
                }
                pixel = maskImage.getRGB(i, j);
                if (!hasTransparency(pixel, threshold)) {
                    mask.add((getX() + i) + "," + (getY() + j));
                }
            }
        }
        return mask;
    }

    public static boolean hasTransparency(int pixel, int threshold) {
        int alpha = (pixel >> 24) & 0xff;
        return alpha < threshold;
    }

    public Optional<Point2D> isCollison(Sprite sprite) {
        return isCollison(sprite, 254);
    }

    public Optional<Point2D> isCollison(Sprite sprite, int transparencyThreshold) {
        if (!visible || isExploding || warpAnimation.isPresent()) {
            return Optional.empty();
        }
        if (!sprite.isVisible() || sprite.isExploding()) {
            return Optional.empty();
        }
        Rectangle2D thisSpriteRectangle = getBounds();
        Rectangle2D incomingSpriteRectangle = sprite.getBounds();
        if (thisSpriteRectangle.intersects(incomingSpriteRectangle)) {
            HashSet<String> thisMask = getMask();
            HashSet<String> incomingMask = sprite.getMask(transparencyThreshold);
            thisMask.retainAll(incomingMask);// Check to see if any pixels in maskPlayer2 are the same as those in maskPlayer1
            if (!thisMask.isEmpty()) {
                String next = thisMask.iterator().next();
                double x = Double.parseDouble(next.split(",")[0]);
                double y = Double.parseDouble(next.split(",")[1]);
                return Optional.of(new Point2D.Double(x, y));
            }
        }
        return Optional.empty();
    }

    public void setExploding(boolean exploding, boolean invisibleAfterExploding) {
        this.isExploding = exploding;
        this.invisibleAfterExploding = invisibleAfterExploding;
    }

    public static BufferedImage createTransparentBufferedImage(int width, int height) {
        // BufferedImage is actually already transparent on my system, but that isn't
        // guaranteed across platforms.
        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        // To be sure, we use clearRect, which will (unlike fillRect) totally replace
        // the current pixels with the desired color, even if it's fully transparent.
        graphics.setBackground(new Color(0, true));
        graphics.clearRect(0, 0, width, height);
        graphics.dispose();

        return bufferedImage;
    }

    public int calculateHitPointsPercentAfterHealthApplied(int amt) {
        if ((hitPoints + amt) > maxHitpoints) {
            this.hitPoints = maxHitpoints;
            return 100;
        }
        hitPoints += amt;
        float percent = (hitPoints * 100.0f) / maxHitpoints;
        return (int) Math.round(percent);
    }

    public int calculateHitPointsPercentAfterDamageApplied(Projectile projectile, Point2D point) {
        hitPoints = hitPoints - projectile.getDamage();
        if (hitPoints <= 0) {
            return 0;
        }
        addDamageAnimation(projectile, point);
        float percent = (hitPoints * 100.0f) / maxHitpoints;
        return (int) Math.round(percent);
    }

    public void addDamageAnimation(Projectile projectile, Point2D point) {
        damageAnimations.add(projectile.getDamageAnimation(removeBoardCoords(point)));
    }

    private Point2D removeBoardCoords(Point2D point) {
        double relativeX = point.getX() - x;
        double relativeY = point.getY() - y;
        return new Point2D.Double(relativeX, relativeY);
    }

    public static ArrayList<Point2D> getLine(Point2D start, Point2D target) {
        ArrayList<Point2D> ret = new ArrayList<>();

        double x0 = start.getX();
        double y0 = start.getY();

        double x1 = target.getX();
        double y1 = target.getY();

        int sx = 0;
        int sy = 0;

        double dx = Math.abs(x1 - x0);
        sx = x0 < x1 ? 1 : -1;
        double dy = -1 * Math.abs(y1 - y0);
        sy = y0 < y1 ? 1 : -1;
        double err = dx + dy, e2; /* error value e_xy */

        for (; ; ) {  /* loop */
            ret.add(new Point2D.Double(x0, y0));
            if (x0 == x1 && y0 == y1) break;
            e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            } /* e_xy+e_x > 0 */
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            } /* e_xy+e_y < 0 */
        }

        return ret;
    }

    enum DownAnglePathDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    public void addDownAnglePath(int xFactor, double speed, int amount, DownAnglePathDirection direction) {
        ArrayList<Point2D> points = Lists.newArrayList();
        if (direction.equals(DownAnglePathDirection.RIGHT_TO_LEFT)) {
            xFactor = -xFactor;
        }
        for (int i = 0; i < amount; i++) {
            double proposedX = (int) (BOARD_X / 3 * sin(i * .5 * Math.PI / (BOARD_X * xFactor))) + previousAddedPoint.getX();
            speed = speed + .3;
            int proposedY = (int) Math.round(speed);
            double x = proposedX;
            double y = proposedY + previousAddedPoint.getY();
            points.add(new Point2D.Double(x, y));
        }
        trajectory.addAll(points);
        previousAddedPoint = points.get(points.size() - 1);
    }

    public void addCircle(double speed, int amount) {
        ArrayList<Point> points = Lists.newArrayList();
        for (int i = 1; i < amount; i++) {
            double orbitalPeriod = 1600;
            double portion = (i % orbitalPeriod) / orbitalPeriod; // [0, 1)
            double angle = portion * 2 * Math.PI;                    // [0, 2 * PI)

            double radius = 80;

            double planetX = previousAddedPoint.getX() + radius * Math.cos(angle);
            double planetY = previousAddedPoint.getY() + radius * Math.sin(angle);

            int newX = (int) Math.round(planetX);
            speed += .1;
            int newY = (int) Math.round(planetY + speed);
            points.add(new Point(newX, newY));
        }
        trajectory.addAll(points);
        previousAddedPoint = points.get(points.size() - 1);
    }

    public void addPoint(int destX, int destY) {
        Point destPoint = new Point(destX, destY);
        trajectory.addAll(getLine(previousAddedPoint, destPoint));
        previousAddedPoint = destPoint;
    }

    public void pause(int amount) {
        for (int i = 0; i < amount * 180; i++) {
            trajectory.add(previousAddedPoint);
        }
    }

    public Point2D getPreviousAddedPoint() {
        return previousAddedPoint;
    }


    public List<SpriteSheetAnimation> getDamageAnimations() {
        return damageAnimations;
    }

    public boolean isExploding() {
        return isExploding;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public static class SpriteRender {
        private final double x;
        private final double y;
        private final BufferedImage image;

        public SpriteRender(double x, double y, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.image = image;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}
