import org.newdawn.slick.Color;

public class Screen {
    private Color[][] grid;

    public Screen(int width, int height, int squareSize) {
        grid = new Color[height / squareSize + 1][width / squareSize + 1];
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

    public void set(int row, int column, Color color) {
        grid[row][column] = color;
    }

    public Color get(int other_x, int other_y) {
        return grid[other_y][other_x];
    }

    int getHeight() {
        return grid.length;
    }

    int getWidth() {
        return grid[0].length;
    }
}
