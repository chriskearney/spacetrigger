package com.comandante.spacetrigger;

import com.comandante.spacetrigger.player.PlayerShip;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class Board extends JPanel implements ActionListener {

    private final int DELAY = 10;
    private Timer timer;
    private PlayerShip playerShip;
    private boolean ingame;

    private long startTime;
    private Level currentLevel;

    private ArrayList<Alien> aliens;
    private ArrayList<Drop> drops;

    private BufferedImage background_3;
    private BufferedImage background_2;
    private BufferedImage background_1;

    private double yOffset_3 = 0;
    private double yDelta_3 = .1;
    private final SplittableRandom random = new SplittableRandom();

    private final EventBus eventBus;

    private int ticks;

    private double yOffset_2 = 0;
    private double yDelta_2 = .2;

    private double yOffset_1 = 0;
    private double yDelta_1 = .3;

    public Board() {
        eventBus = new EventBus();
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new BoardKeyAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        resetBoard();
        timer = new Timer(DELAY, this);
        timer.start();
        background_1 = Assets.BOARD_BACKGROUND_1;
        background_2 = Assets.BOARD_BACKGROUND_2;
        background_3 = Assets.BOARD_BACKGROUND_3;
    }

    private void resetBoard() {
        ingame = true;
        if (playerShip != null) {
            eventBus.unregister(playerShip);
        }
        playerShip = new PlayerShip();
        eventBus.register(playerShip);
        initAliens();
        initDrops();
        startTime = System.currentTimeMillis();
        currentLevel = new LevelOne(eventBus);
    }

    public void initAliens() {
        aliens = Lists.newArrayList();
        ;
    }

    public void initDrops() {
        drops = Lists.newArrayList();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackgrounds(g);
        if (ingame) {
            drawAliens(g);
            drawPlayerShip(g);
            drawDrops(g);
        } else {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawDamageAnimations(Graphics g, Sprite sprite) {
        if (sprite.isExploding() || !sprite.isVisible()) {
            return;
        }
        for (int i = 0; i < sprite.getDamageAnimations().size(); i++) {
            SpriteSheetAnimation spriteDamageAnimations = sprite.getDamageAnimations().get(i);
            Point renderPoint = spriteDamageAnimations.getRenderPoint().get();

            Optional<BufferedImage> currentFrame = spriteDamageAnimations.updateAnimation();
            if (currentFrame.isPresent()) {
                // Ship x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                g.drawImage(currentFrame.get(), (sprite.getX() + renderPoint.getLocation().x) - currentFrame.get().getWidth() / 2, (sprite.getY() + renderPoint.getLocation().y) - currentFrame.get().getHeight() / 2, this);
            } else {
                sprite.getDamageAnimations().remove(i);
            }
        }
    }

    private void drawDrops(Graphics g) {
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Sprite.SpriteRender spriteRender = drop.getSpriteRender();
            g.drawImage(spriteRender.getImage(), spriteRender.getX(), spriteRender.getY(), this);
        }
    }

    private void drawPlayerShip(Graphics g) {
        if (playerShip.isVisible()) {
            Sprite.SpriteRender spaceShipRender = playerShip.getSpriteRender();
            g.drawImage(spaceShipRender.getImage(), spaceShipRender.getX(), spaceShipRender.getY(), this);
            drawDamageAnimations(g, playerShip);
            drawDamageAnimations(g, playerShip.getShield());
            if (playerShip.getShield().isVisible()) {
                Sprite shield = playerShip.getShield();
                shield.setOriginalX(spaceShipRender.getX() + (playerShip.getWidth() / 2) - (playerShip.getShield().getWidth() / 2));
                shield.setOriginalY(spaceShipRender.getY() + (playerShip.getHeight() / 2) - (playerShip.getShield().getHeight() / 2));
                Sprite.SpriteRender spriteRender = shield.getSpriteRender();
                g.drawImage(spriteRender.getImage(), spriteRender.getX(), spriteRender.getY(), this);
            }
            if (playerShip.isMovement() && !playerShip.isExploding()) {
                SpriteSheetAnimation exhaust = playerShip.getExhaust();
                Optional<BufferedImage> currentFrame = exhaust.updateAnimation();
                if (currentFrame.isPresent()) {
                    g.drawImage(currentFrame.get(), spaceShipRender.getX() + (playerShip.getWidth() / 2) + 8, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11, this);
                    g.drawImage(currentFrame.get(), spaceShipRender.getX() + (playerShip.getWidth() / 2) - 16, spaceShipRender.getY() + (playerShip.getHeight() / 2) + 11, this);
                }
            }
        }
        List<Projectile> space = playerShip.getMissiles();
        for (int i = 0; i < space.size(); i++) {
            if (space.get(i).isVisible()) {
                Sprite.SpriteRender spaceShipMissleRender = space.get(i).getSpriteRender();
                g.drawImage(spaceShipMissleRender.getImage(), spaceShipMissleRender.getX(), spaceShipMissleRender.getY(), this);
            }
        }
    }

    private void drawAliens(Graphics g) {
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien.isVisible()) {
                Sprite.SpriteRender aliensRender = alien.getSpriteRender();
                g.drawImage(aliensRender.getImage(), aliensRender.getX(), aliensRender.getY(), this);

                // Alien projectiles
                for (int j = 0; j < alien.getMissiles().size(); j++) {
                    Sprite.SpriteRender alienMissleRender = alien.getMissiles().get(j).getSpriteRender();
                    g.drawImage(alienMissleRender.getImage(), alienMissleRender.getX(), alienMissleRender.getY(), this);
                }

                if (alien.isExploding()) {
                    continue;
                }

                // Damage Animations
                drawDamageAnimations(g, alien);
            }
        }
        g.setColor(Color.WHITE);
        String draw = "Missles: " + playerShip.getCurrentMissles() + " | Health: " + playerShip.getHitPoints();
        g.drawString(draw, 5, 15);
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

        yOffset_3 = incrementOffset(yOffset_3, yDelta_3, background_3.getHeight());
        yOffset_2 = incrementOffset(yOffset_2, yDelta_2, background_2.getHeight());
        yOffset_1 = incrementOffset(yOffset_1, yDelta_1, background_1.getHeight());

        positionBackground(g2d, background_3, yOffset_3);
        positionBackground(g2d, background_2, yOffset_2);
        positionBackground(g2d, background_1, yOffset_1);
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateMissiles();
        updateSpaceShip();
        updateAliens();
        updateDrops();
        checkCollisions();
        repaint();
    }

    public void checkCollisions() {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).isCollison(playerShip).isPresent()) {
                playerShip.setExploding(true, true);
                aliens.get(i).setExploding(true, true);
            }

            List<Projectile> projectiles = aliens.get(i).getMissiles();
            for (int j = 0; j < projectiles.size(); j++) {
                Projectile projectile = projectiles.get(j);
                if (playerShip.isShield()) {
                    playerShip.getShield().setVisible(true);
                    Optional<Point> shieldCollision = projectile.isCollison(playerShip.getShield(), 60);
                    if (shieldCollision.isPresent()) {
                        projectile.setVisible(false);
                        playerShip.getShield().addDamageAnimation(projectile, shieldCollision.get());
                    }
                } else {
                    Optional<Point> collison = projectile.isCollison(playerShip);
                    if (collison.isPresent()) {
                        boolean isDoneFor = playerShip.calculateDamage(projectile, collison.get());
                        projectile.setVisible(false);
                        if (isDoneFor) {
                            playerShip.setExploding(true, true);
                        }
                    }
                }
            }
        }

        List<Projectile> spaceShipMissles = playerShip.getMissiles();
        boolean newExplosion = false;
        for (int i = 0; i < spaceShipMissles.size(); i++) {
            for (int j = 0; j < aliens.size(); j++) {
                Optional<Point> collisonPoint = aliens.get(j).isCollison(spaceShipMissles.get(i));
                if (collisonPoint.isPresent()) {
                    boolean isDoneFor = aliens.get(j).calculateDamage(spaceShipMissles.get(i), collisonPoint.get());
                    spaceShipMissles.get(i).setVisible(false);
                    if (isDoneFor) {
                        processDrops(aliens.get(j));
                        aliens.get(j).setExploding(true, true);
                        newExplosion = true;
                    }
                }
            }
        }

        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            Optional<Point> collison = drop.isCollison(playerShip);
            if (collison.isPresent()) {
                eventBus.post(drop.getEvent());
                drop.setVisible(false);
            }
        }

        if (newExplosion) {
            aliens.sort((abc1, abc2) -> Boolean.compare(abc1.isExploding, abc2.isExploding));
        }
    }

    private void processDrops(Alien alien) {
        int rnd = random.nextInt(0, 100);
        for (int i = 0; i < alien.getDrops().size(); i++) {
            Drop drop = alien.getDrops().get(i);
            int dropPercent = drop.getDropRate().getPercent();
            if (rnd < dropPercent) {
                drop.setOriginalX(alien.getX() + (alien.getWidth() / 2) - (drop.getWidth() / 2));
                drop.setOriginalY(alien.getY() + (alien.getHeight() / 2) - (drop.getHeight() / 2));
                drop.setVisible(true);
                drops.add(drop);
            }
        }
    }


    private void updateMissiles() {

        List<Projectile> spaceShipProjectiles = playerShip.getMissiles();

        for (int i = 0; i < spaceShipProjectiles.size(); i++) {
            Projectile projectile = spaceShipProjectiles.get(i);
            if (projectile.isVisible()) {
                projectile.move();
            } else {
                spaceShipProjectiles.remove(i);
            }
        }

        for (int i = 0; i < aliens.size(); i++) {
            for (int j = 0; j < aliens.get(i).getMissiles().size(); j++) {
                Projectile alienMissle = aliens.get(i).getMissiles().get(j);
                if (alienMissle.isVisible()) {
                    alienMissle.move();
                } else {
                    aliens.get(i).getMissiles().remove(j);
                }
            }
        }
    }

    private void updateSpaceShip() {
        if (!playerShip.isVisible() && !playerShip.isExploding()) {
            ingame = false;
        }
        playerShip.move();
    }

    private void updateAliens() {
        aliens.addAll(currentLevel.getAlien(System.currentTimeMillis() - startTime));

        if (currentLevel.isEmpty() && aliens.isEmpty()) {
            ingame = false;
            return;
        }

        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            if (a.isVisible()) {
                a.move();
            } else {
                eventBus.unregister(a);
                aliens.remove(i);
            }
        }
    }

    private void updateDrops() {
        for (int i = 0; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            if (drop.isVisible()) {
                drop.move();
            } else {
                drops.remove(i);
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
            if (!ingame && e.getKeyCode() == KeyEvent.VK_R) {
                resetBoard();
            } else {
                playerShip.keyPressed(e);
            }
        }
    }
}