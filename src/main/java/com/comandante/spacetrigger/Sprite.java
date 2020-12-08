package com.comandante.spacetrigger;

import com.comandante.spacetrigger.alienscout.AlienScout;
import com.comandante.spacetrigger.player.PlayerShip;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int hitPoints = 0;
    protected boolean visible;
    protected BufferedImage image;
    protected SpriteSheetAnimation explosion;
    protected Optional<SpriteSheetAnimation> warpAnimation = Optional.empty();
    protected boolean isExploding;
    private boolean invisibleAfterExploding;
    protected int ticks = 0;
    protected int speed;
    protected boolean reverse = false;

    protected final SplittableRandom random = new SplittableRandom();

    protected List<SpriteSheetAnimation> damageAnimations = Lists.newArrayList();


    private int centerX;
    private int centerY;

    protected int originalX;
    protected int originalY;

    protected ArrayList<Point> trajectory = Lists.newArrayList();

    private Point previousAddedPoint;

    private static final BufferedImage TRANSPARENT_ONE_PIXEL = createTransparentBufferedImage(1, 1);

    public Sprite(int x, int y, int hitPoints, int speed) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.hitPoints = hitPoints;
        this.speed = speed;
        this.previousAddedPoint = new Point(originalX, originalY);
        this.trajectory.add(previousAddedPoint);
        this.trajectory.add(previousAddedPoint);
    }

    public Sprite(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.speed = speed;
        this.previousAddedPoint = new Point(originalX, originalY);
        this.trajectory.add(previousAddedPoint);
        this.trajectory.add(previousAddedPoint);
    }

    protected void loadImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void move() {
        if (isExploding || warpAnimation.isPresent()) {
            return;
        }
        for (int i = 0; i < speed; i++) {
            Point point = trajectory.get(ticks);
            x = point.getLocation().x;
            y = point.getLocation().y + (int) Math.round(speed);
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
            centerX = getX() + image.getWidth() / 2;
            centerY = getY() + image.getHeight() / 2;
        }

        if (warpAnimation.isPresent()) {
            warpAnimation.get().updateAnimation();
            Optional<BufferedImage> currentFrame = warpAnimation.get().getCurrentFrame();
            if (currentFrame.isPresent()) {
                int warpX = centerX - (currentFrame.get().getWidth() / 2);
                int warpY = centerY - (currentFrame.get().getHeight() / 2);
                return new SpriteRender(warpX, warpY, currentFrame.get());
            } else {
                warpAnimation = Optional.empty();
            }
        }

        if (isExploding) {
            explosion.updateAnimation();
            Optional<BufferedImage> currentFrame = explosion.getCurrentFrame();
            if (currentFrame.isPresent()) {
                int explosionX = centerX - (currentFrame.get().getWidth() / 2);
                int explosionY = centerY - (currentFrame.get().getHeight() / 2);
                return new SpriteRender(explosionX, explosionY, currentFrame.get());
            }

            if (invisibleAfterExploding) {
                setVisible(false);
                isExploding = false;
            }

            return new SpriteRender(x, y, TRANSPARENT_ONE_PIXEL);
        }

        return new SpriteRender(x, y, image);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setOriginalX(int x) {
        this.originalX = x;
        this.x = x;
    }

    public void setOriginalY(int y) {
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

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public HashSet<String> getMask() {
        HashSet<String> mask = new HashSet<String>();
        int pixel;

        for (int i = 0; i < (getWidth()); i++) {
            for (int j = 0; j < getHeight(); j++) {
                pixel = image.getRGB(i, j);
                if (!hasTransparency(pixel)) {
                    mask.add((getX() + i) + "," + (getY() + j));
                }
            }
        }
        return mask;
    }

    public static boolean hasTransparency(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        return alpha < 254;
    }

    public Optional<Point> isCollison(Sprite sprite) {
        if (!visible || isExploding || warpAnimation.isPresent()) {
            return Optional.empty();
        }
        if (!sprite.isVisible() || sprite.isExploding()) {
            return Optional.empty();
        }
        Rectangle thisSpriteRectangle = getBounds();
        Rectangle incomingSpriteRectangle = sprite.getBounds();
        if (thisSpriteRectangle.intersects(incomingSpriteRectangle)) {
            HashSet<String> thisMask = getMask();
            HashSet<String> incomingMask = sprite.getMask();
            thisMask.retainAll(incomingMask);// Check to see if any pixels in maskPlayer2 are the same as those in maskPlayer1
            if (!thisMask.isEmpty()) {
                String next = thisMask.iterator().next();
                int x = Integer.parseInt(next.split(",")[0]);
                int y = Integer.parseInt(next.split(",")[1]);
                return Optional.of(new Point(x, y));
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

    public boolean calculateDamage(Projectile projectile, Point point) {
        hitPoints = hitPoints - projectile.getDamage();
        if (hitPoints <= 0) {
            return true;
        }
        damageAnimations.add(projectile.getDamageAnimation(removeBoardCoords(point)));
        return false;
    }

    private Point removeBoardCoords(Point point) {
        int relativeX = point.getLocation().x - x;
        int relativeY = point.getLocation().y - y;
        return new Point(relativeX, relativeY);
    }

    public static ArrayList<Point> getLine(Point start, Point target) {
        ArrayList<Point> ret = new ArrayList<Point>();

        int x0 = start.x;
        int y0 = start.y;

        int x1 = target.x;
        int y1 = target.y;

        int sx = 0;
        int sy = 0;

        int dx = Math.abs(x1 - x0);
        sx = x0 < x1 ? 1 : -1;
        int dy = -1 * Math.abs(y1 - y0);
        sy = y0 < y1 ? 1 : -1;
        int err = dx + dy, e2; /* error value e_xy */

        for (; ; ) {  /* loop */
            ret.add(new Point(x0, y0));
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
        ArrayList<Point> points = Lists.newArrayList();
        if (direction.equals(DownAnglePathDirection.RIGHT_TO_LEFT)) {
            xFactor = -xFactor;
        }
        for (int i = 0; i < amount; i++) {
            int proposedX = (int) (BOARD_X / 3 * sin(i * .5 * Math.PI / (BOARD_X * xFactor))) + previousAddedPoint.getLocation().x;
            speed = speed + .3;
            int proposedY = (int) Math.round(speed);
            int x = proposedX;
            int y = proposedY + previousAddedPoint.getLocation().y;
            points.add(new Point(x, y));
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

            double planetX = previousAddedPoint.getLocation().x + radius * Math.cos(angle);
            double planetY = previousAddedPoint.getLocation().y + radius * Math.sin(angle);

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

    public Point getPreviousAddedPoint() {
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
        private final int x;
        private final int y;
        private final BufferedImage image;

        public SpriteRender(int x, int y, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.image = image;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}
