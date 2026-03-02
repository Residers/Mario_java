package ru.resider.mario;

import javax.swing.JPanel;
import java.awt.*;
import java.io.Serial;

public class GamePanel extends JPanel implements Runnable {
    @Serial
    private static final long serialVersionUID = 1L;

    private transient Thread gameThread;
    private final transient KeyHandler keyH = new KeyHandler();
    private final transient Player player = new Player(this, keyH);
    private final transient TileManager tileM = new TileManager(this);

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int FPS = 60;
    public static final int TILE_SIZE = 32;

    public static final int MAX_SCREEN_COL = SCREEN_WIDTH / TILE_SIZE;
    public static final int MAX_SCREEN_ROW = SCREEN_HEIGHT / TILE_SIZE;


    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); //делает анимацию более плавной и предотвращает мерцание.
        this.addKeyListener(keyH);
        this.setFocusable(true);// Позволяем панели быть в фокусе

        player.setWorldX(100);
        player.setWorldY(100);
        this.startGameThread();

    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2, player);
        g2.setColor(Color.RED);
        g2.fillRect(player.screenX, player.screenY, TILE_SIZE, TILE_SIZE);

        g2.dispose();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;

        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            update();

            repaint();
            try {
                // 3. Рассчитываем, сколько времени осталось до следующего кадра
                double remainingTime = nextDrawTime - System.nanoTime();

                // 4. Если мы успели все сделать раньше, то "усыпляем" поток,
                // чтобы не тратить ресурсы CPU. Время сна переводим в миллисекунды.
                if (remainingTime > 0) {
                    Thread.sleep((long) (remainingTime / 1000000));
                }
                // 5. Устанавливаем время для следующей отрисовки
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                // Этот блок нужен, если поток будет прерван извне.
                // Пока можно просто вывести информацию об ошибке.
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
