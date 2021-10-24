package com.comandante.spacetrigger;

import com.codahale.metrics.MetricRegistry;
import com.comandante.spacetrigger.events.PlayerShipHealthUpdateEvent;
import com.comandante.spacetrigger.player.PlayerStatusBars;
import com.comandante.spacetrigger.player.PlayerShip;
import com.comandante.spacetrigger.sound.SoundEffectService;
import com.comandante.spacetrigger.sound.SoundEvent;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class Board extends JPanel implements ActionListener {

    private static final int TIMER_DELAY_MS = 10;

    private final EventBus eventBus;
    private final SplittableRandom random = new SplittableRandom();
    private final MetricRegistry metricRegistry;

    private PlayerShip playerShip;
    private Level currentLevel;
    private PlayerStatusBars playerStatusBars;
    private ArrayList<Alien> aliens;
    private ArrayList<Drop> drops;
    private boolean inGame;
    private long startTime;

    private double parallaxBackgroundYOffset_1 = 0;
    private double parallaxBackgroundYDelta_1 = 1.4;
    private BufferedImage background_1;

    private double parallaxBackgroundYOffset_2 = 0;
    private double parallaxBackgroundYDelta_2 = 1;
    private BufferedImage background_2;

    private double parallaxBackgroundYOffset_3 = 0;
    private double parallaxBackgroundYDelta_3 = .9;
    private BufferedImage background_3;

    private final SoundEffectService soundEffectService = new SoundEffectService();

    public Board(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
        this.eventBus = new EventBus();
        eventBus.register(soundEffectService);
        this.initBoard();
    }

    private void initBoard() {
        this.addKeyListener(new BoardKeyAdapter());
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.resetBoard();
        Timer timer = new Timer(TIMER_DELAY_MS, this);
        timer.start();
        this.background_1 = Assets.BOARD_BACKGROUND_1;
        this.background_2 = Assets.BOARD_BACKGROUND_2;
        this.background_3 = Assets.BOARD_BACKGROUND_3;
    }

    private void resetBoard() {
        this.inGame = true;
        if (playerShip != null) {
            eventBus.unregister(playerShip);
        }
        if (playerStatusBars != null) {
            eventBus.unregister(playerStatusBars);
        }
        this.playerShip = new PlayerShip(eventBus);
        this.playerStatusBars = new PlayerStatusBars(eventBus, new PVector(0, 0));
        this.eventBus.register(playerStatusBars);
        this.eventBus.register(playerShip);
        initAliens();
        initDrops();
        this.startTime = System.currentTimeMillis();
        this.currentLevel = new LevelOne(eventBus);
    }

    public void initAliens() {
        aliens = Lists.newArrayList();
    }

    public void initDrops() {
        drops = Lists.newArrayList();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackgrounds(g);
        if (inGame) {
            com.codahale.metrics.Timer.Context time = metricRegistry.timer("statusbar-draw").time();
            drawStatusBars((Graphics2D) g);
            time.stop();

            time = metricRegistry.timer("alien-draw").time();
            drawAliens((Graphics2D) g);
            time.stop();

            time = metricRegistry.timer("playership-draw").time();
            drawPlayerShip((Graphics2D) g);
            time.stop();

            time = metricRegistry.timer("drops-draw").time();
            drawDrops((Graphics2D) g);
            time.stop();

        } else {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawStatusBars(Graphics2D g) {
        Sprite.SpriteRender spriteRender = playerStatusBars.getSpriteRender();
        AffineTransform t = getAffineTransform(spriteRender.getX(), spriteRender.getY());// x/y set here, ball.x/y = double, ie: 10.33
        g.drawImage(spriteRender.getImage(), t, null);
    }

    private void drawDamageAnimations(Graphics2D g, Sprite sprite) {
        if (sprite.isExploding() || !sprite.isVisible()) {
            return;
        }
        for (int i = 0; i < sprite.getDamageAnimations().size(); i++) {
            SpriteSheetAnimation spriteDamageAnimations = sprite.getDamageAnimations().get(i);
            Point2D renderPoint = spriteDamageAnimations.getRenderPoint().get();

            Optional<BufferedImage> currentFrame = spriteDamageAnimations.updateAnimation();
            if (currentFrame.isPresent()) {
                // Ship x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                AffineTransform t = getAffineTransform((sprite.getX() + renderPoint.getX()) - currentFrame.get().getWidth() / 2, (sprite.getY() + renderPoint.getY()) - currentFrame.get().getHeight() / 2);// x/y set here, ball.x/y = double, ie: 10.33
                g.drawImage(currentFrame.get(), t, this);
            } else {
                sprite.getDamageAnimations().remove(i);
            }
        }
    }

    private static AffineTransform getAffineTransform(double x, double y) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y); // x/y set here, ball.x/y = double, ie: 10.33
        t.scale(1, 1); // scale = 1
        return t;
    }

    private void drawDrops(Graphics2D g) {
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Sprite.SpriteRender spriteRender = drop.getSpriteRender();
            AffineTransform t = getAffineTransform(spriteRender.getX(), spriteRender.getY()); // x/y set here, ball.x/y = double, ie: 10.33
            g.drawImage(spriteRender.getImage(), t, this);
        }
    }

    private void drawPlayerShip(Graphics2D g) {
        if (playerShip.isVisible()) {
            Sprite.SpriteRender spaceShipRender = playerShip.getSpriteRender();
            AffineTransform t = getAffineTransform(spaceShipRender.getX(), spaceShipRender.getY());
            g.drawImage(spaceShipRender.getImage(), t, this);
            Sprite shield = playerShip.getShield();
            PVector v = new PVector((spaceShipRender.getX() + (playerShip.getWidth() / 2) - (playerShip.getShield().getWidth() / 2)),
                    (spaceShipRender.getY() + (playerShip.getHeight() / 2) - (playerShip.getShield().getHeight() / 2)));
            shield.setOriginalLocation(v);
            shield.calculateSpriteRender();
            if (playerShip.getShield().isVisible() && !playerShip.isExploding()) {
                Sprite.SpriteRender spriteRender = shield.getSpriteRender();
                AffineTransform transform = getAffineTransform(spriteRender.getX(), spriteRender.getY());
                g.drawImage(spriteRender.getImage(), transform, this);
                drawDamageAnimations(g, playerShip.getShield());
            }
            drawDamageAnimations(g, playerShip);
            if (playerShip.isMovement() && !playerShip.isExploding()) {
                SpriteSheetAnimation exhaust = playerShip.getExhaust();
                Optional<BufferedImage> currentFrame = exhaust.updateAnimation();
                if (currentFrame.isPresent()) {
                    g.drawImage(currentFrame.get(), getAffineTransform(spaceShipRender.getX() + (playerShip.getWidth() / 2) + 8, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11), this);
                    g.drawImage(currentFrame.get(), getAffineTransform(spaceShipRender.getX() + (playerShip.getWidth() / 2) - 16, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11), this);
                }
            }
        }
        List<Projectile> space = playerShip.getMissiles();
        for (int i = 0; i < space.size(); i++) {
            if (space.get(i).isVisible()) {
                Sprite.SpriteRender spaceShipMissleRender = space.get(i).getSpriteRender();
                g.drawImage(spaceShipMissleRender.getImage(), getAffineTransform(spaceShipMissleRender.getX(), spaceShipMissleRender.getY()), this);
            }
        }
    }

    private void drawAliens(Graphics2D g) {
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien.isVisible()) {
                Sprite.SpriteRender aliensRender = alien.getSpriteRender();
                g.drawImage(aliensRender.getImage(), getAffineTransform(aliensRender.getX(), aliensRender.getY()), this);

                // Alien projectiles
                for (int j = 0; j < alien.getProjectiles().size(); j++) {
                    Sprite.SpriteRender alienMissleRender = alien.getProjectiles().get(j).getSpriteRender();
                    g.drawImage(alienMissleRender.getImage(), getAffineTransform(alienMissleRender.getX(), alienMissleRender.getY()), null);
                }
                if (alien.isExploding()) {
                    continue;
                }

                // Damage Animations
                drawDamageAnimations(g, alien);
            }
        }
    }

    private void drawGameOver(Graphics g) {

        String msg = "Game Over - Press \"R\" to restart";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BOARD_X - fm.stringWidth(msg)) / 2, BOARD_Y / 2);
    }

    private static void positionBackground(Graphics2D g2d, BufferedImage bg, double yOffset) {
        if (bg != null) {
            int xPos = (BOARD_X - bg.getWidth()) / 2;
            int yPos = (int) Math.round(yOffset);

            while (yPos > 0) {
                yPos -= bg.getHeight();
                g2d.drawImage(bg, xPos, yPos, null);
            }

            yPos = (int) Math.round(yOffset);
            while (yPos < BOARD_Y) {
                g2d.drawImage(bg, xPos, yPos, null);
                yPos += bg.getHeight();
            }
        }
    }

    private static double incrementOffset(double offSet, double delta, int maxHeight) {
        offSet += delta;
        if (offSet > maxHeight) {
            offSet = 0;
        }
        return offSet;
    }

    private void drawBackgrounds(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        parallaxBackgroundYOffset_3 = incrementOffset(parallaxBackgroundYOffset_3, parallaxBackgroundYDelta_3, background_3.getHeight());
        parallaxBackgroundYOffset_2 = incrementOffset(parallaxBackgroundYOffset_2, parallaxBackgroundYDelta_2, background_2.getHeight());
        parallaxBackgroundYOffset_1 = incrementOffset(parallaxBackgroundYOffset_1, parallaxBackgroundYDelta_1, background_1.getHeight());

        positionBackground(g2d, background_3, parallaxBackgroundYOffset_3);
        positionBackground(g2d, background_2, parallaxBackgroundYOffset_2);
        positionBackground(g2d, background_1, parallaxBackgroundYOffset_1);
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerStatusBars.update();
        updateMissiles();
        updateSpaceShip();
        updateAliens();
        updateDrops();
        checkCollisions();
        repaint();
    }


    interface AlienExecute {
        void process(Alien alien);
    }

    public interface ProjectileExecute {
        void process(Projectile projectile);
    }

    private List<Projectile> getAlienProjectiles() {
        List<Projectile> projectiles = Lists.newArrayList();
        runOnAllAliens(alien -> projectiles.addAll(alien.getProjectiles()));
        return projectiles;
    }

    private void runOnAllAliens(AlienExecute alienExecute) {
        for (Alien alien : aliens) {
            alienExecute.process(alien);
        }
    }

    private void runOnAllAlienProjectiles(ProjectileExecute projectileExecute) {
        for (Projectile alienProjectile : getAlienProjectiles()) {
            projectileExecute.process(alienProjectile);
        }
    }

    public void checkCollisions() {
        final boolean[] newExplosion = {false};

        runOnAllAlienProjectiles(projectile -> {
            if (!projectile.isVisible()) {
                return;
            }
            // Check if alien projectiles hit playerShip
            Optional<Point2D> shieldCollision = projectile.isCollison(playerShip.getShield(), 100, true);
            if (shieldCollision.isPresent() && playerShip.getCurrentShield() > 20) {
                playerShip.getShield().setVisible(true);
                projectile.setVisible(false);
                playerShip.getShield().addDamageAnimation(projectile, shieldCollision.get());
                eventBus.post(new SoundEvent(Assets.PLAYER_SHIELD_IMPACT_SOUND));
            } else {
                Optional<Point2D> collison = projectile.isCollison(playerShip);
                if (collison.isPresent()) {
                    int newHitPointsPct = playerShip.calculateHitPointsPercentAfterDamageApplied(projectile, collison.get());
                    projectile.setVisible(false);
                    if (newHitPointsPct == 0) {
                        playerShip.setExploding(true, true);
                    }
                    eventBus.post(new PlayerShipHealthUpdateEvent(newHitPointsPct));
                }
            }

            // Check if alien projectile hit other alien
            runOnAllAliens(alienCheck -> {
                // this is a kind of sloppy way to prevent alien friendly fire
                if (!projectile.isOlderThan(1, TimeUnit.SECONDS)) {
                    return;
                }
                Optional<Point2D> collisonPoint = alienCheck.isCollison(projectile);
                if (collisonPoint.isPresent()) {
                    int newHitPointsPercentage = alienCheck.calculateHitPointsPercentAfterDamageApplied(projectile, collisonPoint.get());
                    projectile.setVisible(false);
                    if (newHitPointsPercentage <= 0) {
                        processDrops(alienCheck);
                        alienCheck.setExploding(true, true);
                        newExplosion[0] = true;
                    }
                }
            });
        });

        runOnAllAliens(alien -> {
            if (alien.isCollison(playerShip).isPresent()) {
                playerShip.setExploding(true, true);
                alien.setExploding(true, true);
            }
        });

        for (Projectile spaceShipMissle : playerShip.getMissiles()) {
            runOnAllAliens(alien -> {
                Optional<Point2D> collisonPoint = alien.isCollison(spaceShipMissle);
                if (collisonPoint.isPresent()) {
                    int newHitPointsPercentage = alien.calculateHitPointsPercentAfterDamageApplied(spaceShipMissle, collisonPoint.get());
                    spaceShipMissle.setVisible(false);
                    if (newHitPointsPercentage <= 0) {
                        processDrops(alien);
                        alien.setExploding(true, true);
                        newExplosion[0] = true;
                    }
                }
            });

            runOnAllAlienProjectiles(projectile -> {
                if (projectile.isCollison(spaceShipMissle).isPresent()) {
                    spaceShipMissle.setVisible(false);
                    projectile.setExploding(true, true);
                }
            });
        }

        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Optional<Point2D> collison = drop.isCollison(playerShip);
            if (collison.isPresent()) {
                eventBus.post(drop.getEvent());
                drop.setVisible(false);
            }
        }

        if (newExplosion[0]) {
            aliens.sort((abc1, abc2) -> Boolean.compare(abc1.isExploding, abc2.isExploding));
        }
    }

    private void processDrops(Alien alien) {
        int rnd = random.nextInt(0, 100);
        for (int i = 0; i < alien.getDrops().size(); i++) {
            Drop drop = alien.getDrops().get(i);
            int dropPercent = drop.getDropRate().getPercent();
            if (rnd < dropPercent) {
                drop.setOriginalLocation(new PVector(alien.getX() + (alien.getWidth() / 2) - (drop.getWidth() / 2),
                        alien.getY() + (alien.getHeight() / 2) - (drop.getHeight() / 2)));
                drop.calculateSpriteRender();
                drop.setVisible(true);
                drops.add(drop);
                return;
            }
        }
    }


    private void updateMissiles() {

        List<Projectile> spaceShipProjectiles = playerShip.getMissiles();

        for (int i = 0; i < spaceShipProjectiles.size(); i++) {
            Projectile projectile = spaceShipProjectiles.get(i);
            if (projectile.isVisible()) {
                projectile.update();
            }
        }

        spaceShipProjectiles.removeIf(s -> !s.isVisible());

        for (int i = 0; i < aliens.size(); i++) {
            boolean remove = false;
            for (int j = 0; j < aliens.get(i).getProjectiles().size(); j++) {
                Projectile alienMissle = aliens.get(i).getProjectiles().get(j);
                if (alienMissle.isVisible()) {
                    alienMissle.update();
                } else {
                    remove = true;
                    try {
                        eventBus.unregister(alienMissle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (remove) {
                aliens.get(i).getProjectiles().removeIf(missle -> !missle.isVisible());
            }
        }


    }

    private void updateSpaceShip() {
        if (!playerShip.isVisible() && !playerShip.isExploding()) {
            inGame = false;
        }

        if (playerShip.getShield().getDamageAnimations().size() == 0) {
            playerShip.getShield().setVisible(false);
        }

        playerShip.update();
    }

    private void updateAliens() {
        aliens.addAll(currentLevel.getAlien(System.currentTimeMillis() - startTime));

        if (currentLevel.isEmpty() && aliens.isEmpty()) {
            inGame = false;
            return;
        }

        boolean remove = false;
        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            if (a.isVisible()) {
                a.update();
            } else {
                remove = true;
            }
        }

        if (remove) {
            Iterator<Alien> iterator = aliens.iterator();
            while (iterator.hasNext()) {
                Alien next = iterator.next();
                if (!next.isVisible()) {
                    eventBus.unregister(next);
                    iterator.remove();
                }
            }
        }
    }

    private void updateDrops() {
        boolean remove = false;
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            if (drop.isVisible()) {
                drop.update();
            } else {
                remove = true;
            }
        }

        if (remove) {
            Iterator<Drop> iterator = drops.iterator();
            while (iterator.hasNext()) {
                Drop next = iterator.next();
                if (!next.isVisible()) {
                    iterator.remove();
                }
            }
        }
    }


    private class BoardKeyAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            playerShip.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!inGame && e.getKeyCode() == KeyEvent.VK_R) {
                resetBoard();
            } else {
                playerShip.keyPressed(e);
            }
        }
    }
}