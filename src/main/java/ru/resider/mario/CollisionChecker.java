package ru.resider.mario;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Player player) {
        // --- ВЫЧИСЛЯЕМ КООРДИНАТЫ ГРАНИЦ HITBOX В МИРЕ ---
        int entityLeftWorldX = player.getWorldX() + player.solidArea.x;
        int entityRightWorldX = player.getWorldX() + player.solidArea.x + player.solidArea.width;
        int entityTopWorldY = (int) (player.getWorldY() + player.solidArea.y);
        int entityBottomWorldY = (int) (player.getWorldY() + player.solidArea.y + player.solidArea.height);

        // --- ВЫЧИСЛЯЕМ, В КАКИХ ПЛИТКАХ НАХОДЯТСЯ ЭТИ ГРАНИЦЫ ---
        int entityLeftCol = entityLeftWorldX / GamePanel.TILE_SIZE;
        int entityRightCol = entityRightWorldX / GamePanel.TILE_SIZE;
        int entityTopRow = entityTopWorldY / GamePanel.TILE_SIZE;
        int entityBottomRow = entityBottomWorldY / GamePanel.TILE_SIZE;

        int tileNum1, tileNum2;
    }
}
