package ru.resider.mario;

import javax.swing.JPanel;
import java.awt.*;
import java.io.Serial;

public class GamePanel extends JPanel implements Runnable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient Thread gameThread;
    private final transient KeyHandler keyH = new KeyHandler();
    private final transient Player player = new Player(keyH);

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int FPS = 60;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); //делает анимацию более плавной и предотвращает мерцание.
        this.addKeyListener(keyH);
        this.setFocusable(true);// Позволяем панели быть в фокусе

        player.setX(100);
        player.setY(100);
        this.startGameThread();

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
        g.setColor(Color.RED);
        g.fillRect(player.getX(),(int) player.getY(), 50, 50);
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
