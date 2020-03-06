import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.Random;

class Screen {
    private Color[][] grid;
    private Random random = new Random();

    int getSquareSize() {
        return squareSize;
    }

    private int squareSize;

    Screen(int width, int height, int squareSize) {
        grid = new Color[height / squareSize + 1][width / squareSize + 1];
        this.squareSize = squareSize;
    }

    void placeInitialColoredDots() {
        Color[] colors = {Color.blue, Color.green.darker(), Color.orange, Color.magenta, Color.red, Color.cyan};
        for (int i = 0; i < colors.length; ++i)
            set(random.nextInt(getHeight()), random.nextInt(getWidth()), colors[i % colors.length]);
    }

    void addRandomDotsOfCopiedColors() {
        final int max = getHeight() * getWidth();
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            int other_x = x + random.nextInt(3) - 1;
            int other_y = y + random.nextInt(3) - 1;
            if ((other_x != x || other_y != y) && isInside(other_x, other_y)) {
                final Color other = get(other_x, other_y);
                if (other != null && other != Color.white && get(x, y) != Color.white)
                    set(y, x, other);
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
                set(y1, x, Color.white);
        } else {
            for (int y = y1; y <= y2; ++y)
                set(y, x1, Color.white);
        }
    }

    void removeWalls(int x1, int x2, int y1, int y2) {
        for (int yy = y1 + 1; yy < y2; ++yy) {
            for (int xx = x1 + 1; xx < x2; ++xx) {
                if (get(xx, yy) == Color.white)
                    set(yy, xx, null);
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
                set(yy, xx, null);
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

    void set(int row, int column, Color color) {
        grid[row][column] = color;
    }

    Color get(int other_x, int other_y) {
        return grid[other_y][other_x];
    }

    int getHeight() {
        return grid.length;
    }

    int getWidth() {
        return grid[0].length;
    }
}
