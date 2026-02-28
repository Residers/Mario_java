package ru.resider.mario;

public class Player {
    private int x;
    private double y;
    private double velocityY;
    private static final double GRAVITY = 0.5;
    private int jumpLeft = 2;
    private final int speed = 4;
    private final KeyHandler keyH;

    public Player(KeyHandler keyH) {
        this.keyH = keyH;
    }

    public int getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void update() {
        if (keyH.isLeftPressed()) {
            x -= speed;
        }
        if (keyH.isRightPressed()) {
            x += speed;
        }
        if (keyH.isJumpPressed() && jumpLeft > 0) {
            velocityY = -15;
            jumpLeft--;

            // Хитрость: чтобы прыжок не срабатывал каждый кадр, пока зажата клавиша
            keyH.setJumpPressed(false);
        }
        velocityY += GRAVITY;
        y += velocityY;
        if (y > 550) {
            y = 550;
            velocityY = 0;
            jumpLeft = 2;
        }
    }
}
