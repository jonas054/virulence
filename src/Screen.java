import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.Random;

class Screen {
    private Color[][] grid;
    private Random random = new Random();
    private final Color[] colors = {
            Color.blue, Color.green.darker(), Color.orange, Color.magenta, Color.red, Color.cyan
    };
    private final String[] colorNames = {
            "blue", "green", "yellow", "magenta", "red", "cyan"
    };

    int getSquareSize() {
        return squareSize;
    }

    private int squareSize;

    Screen(int width, int height, int squareSize) {
        grid = new Color[height / squareSize + 1][width / squareSize + 1];
        this.squareSize = squareSize;
    }

    void placeInitialColoredDots() {
        for (int i = 0; i < colors.length; ++i)
            set(random.nextInt(getWidth()), random.nextInt(getHeight()), colors[i % colors.length]);
    }

    void addRandomDotsOfCopiedColors() {
        final int max = getHeight() * getWidth();
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            int otherX = x + random.nextInt(3) - 1;
            int otherY = y + random.nextInt(3) - 1;
            if ((otherX != x || otherY != y) && isInside(otherX, otherY)) {
                final Color otherColor = get(otherX, otherY);
                if (otherColor != null && otherColor != Color.white && get(x, y) != Color.white)
                    set(x, y, otherColor);
            }
        }
    }

    void drawFourWalls(int x1, int x2, int y1, int y2) {
        drawLine(x1, y1, x2, y1);
        drawLine(x1, y2, x2, y2);
        drawLine(x1, y1, x1, y2);
        drawLine(x2, y1, x2, y2);
    }

    void drawLine(int x1, int y1, int x2, int y2) {
        if (y1 == y2) {
            for (int x = x1; x <= x2; ++x)
                set(x, y1, Color.white);
        } else {
            for (int y = y1; y <= y2; ++y)
                set(x1, y, Color.white);
        }
    }

    void removeWalls(int x1, int x2, int y1, int y2) {
        for (int yy = y1 + 1; yy < y2; ++yy) {
            for (int xx = x1 + 1; xx < x2; ++xx) {
                if (get(xx, yy) == Color.white)
                    set(xx, yy, null);
            }
        }
    }

    boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    void paint(Graphics g) {
        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++) {
                final Color color = get(x, y);
                if (color != null) {
                    g.setColor(color);
                    g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                }
            }
    }

    void erase(int row, int column, int radius) {
        int x1 = Main.limits(column - radius, getWidth());
        int x2 = Main.limits(column + radius, getWidth());
        int y1 = Main.limits(row - radius, getHeight());
        int y2 = Main.limits(row + radius, getHeight());
        for (int yy = y1; yy <= y2; ++yy) {
            for (int xx = x1; xx <= x2; ++xx)
                set(xx, yy, null);
        }
    }

    void buildCage(int row, int column, int radius) {
        int x1 = Main.limits(column - radius, getWidth());
        int x2 = Main.limits(column + radius, getWidth());
        int y1 = Main.limits(row - radius, getHeight());
        int y2 = Main.limits(row + radius, getHeight());
        drawFourWalls(x1, x2, y1, y2);
        removeWalls(x1, x2, y1, y2);
    }

    void set(int column, int row, Color color) {
        grid[row][column] = color;
    }

    Color get(int column, int row) {
        return grid[row][column];
    }

    int getHeight() {
        return grid.length;
    }

    int getWidth() {
        return grid[0].length;
    }

    public String calculateScore() {
        String result = "";
        int[] score = new int[colors.length];
        int total = 0;
        for (Color[] row : grid) {
            for (Color cell : row) {
                if (cell != null && cell != Color.white) {
                    total++;
                }
            }
        }
        for (int i = 0; i < colors.length; ++i) {
            Color color = colors[i];
            result += colorNames[i] + ": ";
            for (Color[] row : grid) {
                for (Color cell : row) {
                    if (cell == color) {
                        score[i]++;
                    }
                }
            }
            result += String.format("%.0f%%  ", 100.0 * score[i] / total);
        }
        return result;
    }
}
