import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    private int squareSize;

    Screen(int width, int height, int squareSize) {
        grid = new Color[height / squareSize][width / squareSize];
        this.squareSize = squareSize;
    }

    void placeInitialColoredDots() {
        for (int i = 0; i < colors.length; ++i)
            set(random.nextInt(getWidth()), random.nextInt(getHeight()), colors[i]);
    }

    void addRandomDotsOfCopiedColors() {
        final int height = getHeight();
        final int width = getWidth();
        final int max = height * width;
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
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
        x1 = Main.limits(x1, getWidth());
        x2 = Main.limits(x2, getWidth());
        y1 = Main.limits(y1, getHeight());
        y2 = Main.limits(y2, getHeight());
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2) {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        drawLine(x1, y1, x2, y1);
        drawLine(x1, y2, x2, y2);
        drawLine(x1, y1, x1, y2);
        drawLine(x2, y1, x2, y2);
        removeWalls(x1, x2, y1, y2);
    }

    void drawLine(int x1, int y1, int x2, int y2) {
        if (y1 == y2)
            for (int x = x1; x <= x2; ++x)
                set(x, y1, Color.white);
        else
            for (int y = y1; y <= y2; ++y)
                set(x1, y, Color.white);
    }

    void removeWalls(int x1, int x2, int y1, int y2) {
        for (int yy = y1 + 1; yy < y2; ++yy)
            for (int xx = x1 + 1; xx < x2; ++xx)
                if (get(xx, yy) == Color.white)
                    set(xx, yy, null);
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
        for (int yy = y1; yy <= y2; ++yy)
            for (int xx = x1; xx <= x2; ++xx)
                set(xx, yy, null);
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

    public List<String> calculateScore() {
        int[] score = new int[colors.length];
        int total = 0;
        for (int i = 0; i < colors.length; ++i) {
            Color color = colors[i];
            for (Color[] row : grid)
                for (Color cell : row)
                    if (cell == color) {
                        score[i]++;
                        if (cell != null && cell != Color.white)
                            total++;
                    }
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < colors.length; ++i)
            if (score[i] > 0)
                result.add(String.format("%4.1f%% %s", 100.0 * score[i] / total, colorNames[i]));
        result.sort(Comparator.<String>reverseOrder());
        return result;
    }

    public void drawOutline(Graphics g, Point firstPoint, Point secondPoint) {
        float brightness = random.nextFloat() / 3 + 0.33F;
        g.setColor(new Color(brightness, brightness, brightness));
        g.drawRect(firstPoint.x, firstPoint.y,
                   secondPoint.x - firstPoint.x, secondPoint.y - firstPoint.y);
    }
}
