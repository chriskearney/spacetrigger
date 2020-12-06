package com.comandante.spacetrigger;

import com.comandante.spacetrigger.player.PlayerShip;

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

import static com.comandante.spacetrigger.Main.BOARD_X;
import static com.comandante.spacetrigger.Main.BOARD_Y;

public class Board extends JPanel implements ActionListener {

    private final int DELAY = 10;
    private Timer timer;
    private PlayerShip playerShip;
    private BufferedImage backgroundImage;
    private boolean ingame;

    private long startTime;
    private Level currentLevel = new LevelOne();

    private ArrayList<Alien> aliens;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new BoardKeyAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        resetBoard();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void resetBoard() {
        this.backgroundImage = Assets.BOARD_BACKGROUND_1;
        ingame = true;
        playerShip = new PlayerShip();
        initAliens();
        startTime = System.currentTimeMillis();
        currentLevel = new LevelOne();
    }

    public void initAliens() {
        aliens = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ingame) {
            drawObjects(g);
        } else {
           drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }


    private void drawObjects(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);

        if (playerShip.isVisible()) {
            Sprite.SpriteRender spaceShipRender = playerShip.getSpriteRender();
            g.drawImage(spaceShipRender.getImage(), spaceShipRender.getX(), spaceShipRender.getY(), this);
            for (int i = 0; i < playerShip.getDamageAnimations().size(); i++) {
                SpriteSheetAnimation alienDamageAnimation = playerShip.getDamageAnimations().get(i);
                Point renderPoint = alienDamageAnimation.getRenderPoint().get();
                alienDamageAnimation.updateAnimation();
                Optional<BufferedImage> currentFrame = alienDamageAnimation.getCurrentFrame();
                if (currentFrame.isPresent()) {
                    // Ship x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                    g.drawImage(currentFrame.get(), (playerShip.getX() + renderPoint.getLocation().x) - currentFrame.get().getWidth() / 2, (playerShip.getY() + renderPoint.getLocation().y) - currentFrame.get().getHeight() / 2, this);
                } else {
                    playerShip.getDamageAnimations().remove(i);
                }
            }
        }

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
                List<SpriteSheetAnimation> damageAnimations = alien.getDamageAnimations();
                for (int j = 0; j < damageAnimations.size(); j++) {
                    SpriteSheetAnimation alienDamageAnimation = damageAnimations.get(j);
                    Point renderPoint = alienDamageAnimation.getRenderPoint().get();
                    alienDamageAnimation.updateAnimation();
                    Optional<BufferedImage> currentFrame = alienDamageAnimation.getCurrentFrame();
                    if (currentFrame.isPresent()) {
                        // Alien x/y position on the board + its relative position on the sprite - the /2 of the width/height of damage animation - centers the damage animation on the x/y collision point
                        g.drawImage(currentFrame.get(), (alien.getX() + renderPoint.getLocation().x) - currentFrame.get().getWidth() / 2, (alien.getY() + renderPoint.getLocation().y) - currentFrame.get().getHeight() / 2, this);
                    } else {
                        alien.getDamageAnimations().remove(j);
                    }
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

    @Override
    public void actionPerformed(ActionEvent e) {
        updateMissiles();
        updateSpaceShip();
        updateAliens();
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

        List<Projectile> spaceShipMissles = playerShip.getMissiles();
        boolean newExplosion = false;
        for (int i = 0; i < spaceShipMissles.size(); i++) {
            for (int j = 0; j < aliens.size(); j++) {
                Optional<Point> collisonPoint = aliens.get(j).isCollison(spaceShipMissles.get(i));
                if (collisonPoint.isPresent()) {
                    boolean isDoneFor = aliens.get(j).calculateDamage(spaceShipMissles.get(i), collisonPoint.get());
                    spaceShipMissles.get(i).setVisible(false);
                    if (isDoneFor) {
                        aliens.get(j).setExploding(true, true);
                        newExplosion = true;
                    }
                }
            }
        }

        if (newExplosion) {
            aliens.sort((abc1, abc2) -> Boolean.compare(abc1.isExploding, abc2.isExploding));
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
                aliens.remove(i);
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