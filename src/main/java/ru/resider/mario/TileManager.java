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
        mapTileNum = new int[GamePanel.MAX_SCREEN_COL][GamePanel.MAX_SCREEN_ROW];
        getTileImage();

        loadMap();
    }

    public void loadMap() {
        try {
            // Получаем доступ к файлу карты
            InputStream is = getClass().getResourceAsStream("/maps/world01.txt");
            // Создаем "читателя" для удобной работы с текстовым файлом
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (row < GamePanel.MAX_SCREEN_ROW) {
                // Читаем одну строку из файла
                String line = br.readLine();
                String[] numbers = line.split(" ");

                while (col < GamePanel.MAX_SCREEN_COL) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                col = 0;
                row++;
            }
            br.close(); // Закрываем "читателя"

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, Player player) {
        int col = 0;
        int row = 0;


        while (col < GamePanel.MAX_SCREEN_COL && row < GamePanel.MAX_SCREEN_ROW) {
            int tileNum = mapTileNum[col][row];

            int worldX = col * GamePanel.getTileSize();
            int worldY = row * GamePanel.getTileSize();

            // Координаты плитки на экране.
            // Мы вычитаем worldX игрока и добавляем его screenX.
            // Это создает эффект камеры.
            int screenX = worldX - player.getWorldX() + player.screenX;
            int screenY = (int) (worldY - player.getWorldY() + player.screenY);

            g2.drawImage(tile[tileNum].getImage(), screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
            col++;

            if (col == GamePanel.MAX_SCREEN_COL) {
                col = 0;
                row++;
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
