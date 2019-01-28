package com.company;

import java.util.Random;

class Renderer {

    private int width, height;
    private int[] pixels;
    private static final int MAP_SIZE = 8;
    private static final int MAP_SIZE_MASK = MAP_SIZE - 1;
    private int[] tiles = new int[MAP_SIZE * MAP_SIZE];
    private static final Random random = new Random();

    Renderer(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        fill();
    }

    private void fill() {
        for(int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
            tiles[i] = random.nextInt(0xfffffff);
            tiles[0] = 0;
        }
    }

    void clear(){
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    void render(int xOffset, int yOffset) {
        for(int y = 0; y < height; y++) {
            int yy = y + yOffset;
            for(int x = 0; x < width; x++) {
                int xx = x + xOffset;
                int tileIndex = ((yy >> 4) & MAP_SIZE_MASK) + (((xx >> 4) & MAP_SIZE_MASK) * MAP_SIZE);
                pixels[x + (y * width)] = tiles[tileIndex];
            }
        }
    }
}
