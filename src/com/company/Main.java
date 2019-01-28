package com.company;

import com.company.input.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

@SuppressWarnings("serial")
public class Main extends Canvas {

    //   private static final long serialVersionUID = 1L;

    private static final int WIDTH = 300;
    private static final int HEIGHT = WIDTH / 16 * 9;
    private static final int SCALE = 3;
    private String title = "Game";
    private boolean running = false;
    private BufferStrategy bs = null;
    private Renderer renderer;
    private JFrame frame = new JFrame();
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int x = 0, y = 0;

    private Main() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        renderer = new Renderer(WIDTH, HEIGHT, pixels);
        frame.addKeyListener(new InputHandler());
    }

    private void start () {
        running = true;
        init();
        new Thread(() -> {
            long jvmLastTime = System.nanoTime();
            long time = System.currentTimeMillis();
            double jvmPartTime = 1_000_000_000.0 / 60.0;
            double delta = 0;
            int updates = 0;
            int frames = 0;
            while (running) {
                long jvmNow = System.nanoTime();
                delta += (jvmNow - jvmLastTime);
                jvmLastTime = jvmNow;
                if (delta >= jvmPartTime) {
                    update();
                    updates++;
                    delta = 0;
                } render();
                frames++;
                if (System.currentTimeMillis() - time > 1000) {
                    time += 1000;
                    frame.setTitle(title + " | " + "Updates: " + updates + ", " + "Frames: " + frames);
                    updates = 0;
                    frames  = 0;
                }
            }
        }).start();
    }

    private void update() {
        if (InputHandler.isKeyPressed(KeyEvent.VK_UP)) y--;
        if (InputHandler.isKeyPressed(KeyEvent.VK_DOWN)) y++;
        if (InputHandler.isKeyPressed(KeyEvent.VK_RIGHT)) x++;
        if (InputHandler.isKeyPressed(KeyEvent.VK_LEFT)) x--;
    }

    private void render() {
        if (bs == null) {
            createBufferStrategy(3);
            bs = getBufferStrategy();
        }

        renderer.clear();
        renderer.render(x, y);
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bufferSwap();
    }

    private void bufferSwap() {
        bs.show();
    }

    private void init() {
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main().start();
    }
}