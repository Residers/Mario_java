package ru.resider.mario;

import java.awt.Rectangle;

public class Player {
    public final int screenX;
    public final int screenY;
    private final GamePanel gp;
    private int worldX;
    private double worldY;
    private double velocityY;
    private static final double GRAVITY = 0.5;
    private int jumpLeft = 2;
    private final int speed = 4;
    private final KeyHandler keyH;
    public Rectangle solidArea;
    public boolean collisionOn = false;
    private CollisionChecker cChecker;
    public String direction;

    public Player(GamePanel gp, KeyHandler keyH, CollisionChecker cChecker) {
        this.keyH = keyH;
        this.gp = gp;
        this.cChecker = cChecker;
        // Устанавливаем игрока в центр экрана
        this.screenX = GamePanel.getScreenWidth() / 2 - (GamePanel.getTileSize() / 2);
        this.screenY = GamePanel.getScreenHeight() / 2 - (GamePanel.getTileSize() / 2);

        //solidArea будет описывать область внутри спрайта, которая считается "твердой".
        solidArea = new Rectangle(8, 16, 16, 16);
        direction = "down";
    }

    public int getSpeed(){
        return speed;
    }

    public int getWorldX() {
        return worldX;
    }

    public double getWorldY() {
        return worldY;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public void setWorldY(double worldY) {
        this.worldY = worldY;
    }

    public void update() {

        // --- ПРОВЕРКА СТОЛКНОВЕНИЙ ---
        collisionOn = false;
        cChecker.checkTile(this); // Вызываем проверку

        if (keyH.isLeftPressed()) {
            direction = "left";
            if (!collisionOn) {
                worldX -= speed;
            }
        }
        if (keyH.isRightPressed()) {
            direction = "right";
            if (!collisionOn) {
                worldX += speed;
            }
        }
        if (keyH.isJumpPressed() && jumpLeft > 0) {
            velocityY = -15;
            jumpLeft--;

            // Хитрость: чтобы прыжок не срабатывал каждый кадр, пока зажата клавиша
            keyH.setJumpPressed(false);
        }
        velocityY += GRAVITY;
        worldY += velocityY;
        if (worldY > 550) {
            worldY = 550;
            velocityY = 0;
            jumpLeft = 2;
        }
    }
}
