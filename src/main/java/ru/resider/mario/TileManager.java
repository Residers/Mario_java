package ru.resider.mario;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {

    private GamePanel gp;
    public Tile[] tile;
    private int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[GamePanel.MAX_WORLD_COL][GamePanel.MAX_WORLD_ROW];
        getTileImage();

        loadMap();
    }

    public void loadMap() {
        try {
            // Получаем доступ к файлу карты
            InputStream is = getClass().getResourceAsStream("/maps/world01.txt");
            // Создаем "читателя" для удобной работы с текстовым файлом
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)));

            for (int row = 0; row < GamePanel.MAX_WORLD_ROW; row++) {
                String line = br.readLine();
                // Если файл закончился раньше, чем мы ожидали, прекращаем работу
                if (line == null) {
                    break;
                }

                String[] numbers = line.split(" ");

                for (int col = 0; col < GamePanel.MAX_WORLD_COL; col++) {
                    // Если в строке файла меньше чисел, чем мы ожидаем,
                    // просто используем 0 (земля) для оставшихся ячеек.
                    if (col < numbers.length) {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNum[col][row] = num;
                    }
                }
            }
            br.close(); // Закрываем "читателя"

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, Player player) {
        // --- ОПРЕДЕЛЯЕМ ГРАНИЦЫ ВИДИМОЙ ОБЛАСТИ ---
        // Левая граница (какая колонка мира видна слева на экране)
        int worldColStart = (player.getWorldX() - player.screenX) / GamePanel.TILE_SIZE;
        // Правая граница
        int worldColEnd = (player.getWorldX() + player.screenX) / GamePanel.TILE_SIZE + 2; // +2 для буфера
        // Верхняя граница
        int worldRowStart = (int) ((player.getWorldY() - player.screenY) / GamePanel.TILE_SIZE);
        // Нижняя граница
        int worldRowEnd = (int) ((player.getWorldY() + player.screenY) / GamePanel.TILE_SIZE) + 2; // +2 для буфера

        for (int worldRow = worldRowStart; worldRow < worldRowEnd; worldRow++) {
            for (int worldCol = worldColStart; worldCol < worldColEnd; worldCol++) {

                // Пропускаем отрисовку, если вышли за пределы карты
                if (worldCol < 0 || worldCol >= GamePanel.MAX_WORLD_COL ||
                        worldRow < 0 || worldRow >= GamePanel.MAX_WORLD_ROW) {
                    continue;
                }

                int tileNum = mapTileNum[worldCol][worldRow];
                // Координаты плитки в мире
                int worldX = worldCol * GamePanel.TILE_SIZE;
                int worldY = worldRow * GamePanel.TILE_SIZE;

                // Координаты плитки на экране (формула та же)
                int screenX = worldX - player.getWorldX() + player.screenX;
                int screenY = (int) (worldY - player.getWorldY() + player.screenY);

                // Рисуем плитку, только если она находится в пределах экрана
                // Это дополнительная оптимизация, чтобы не рисовать то, что уже за кадром
                if (screenX > -GamePanel.TILE_SIZE && screenX < GamePanel.getScreenWidth() &&
                        screenY > -GamePanel.TILE_SIZE && screenY < GamePanel.getScreenHeight()) {
                    g2.drawImage(tile[tileNum].getImage(), screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
                }
            }
        }
    }


    public void getTileImage() {
        try {
            setupTile(0, "earth", false);
            setupTile(1, "wall", true);
            setupTile(2, "water", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTile(int index, String imageName, boolean collision) throws IOException {
        tile[index] = new Tile();
        tile[index].setCollision(collision);
        String imagePath = "/tiles/" + imageName + ".png";

        tile[index].setImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));


    }
}
