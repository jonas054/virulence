import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class Main extends BasicGame {
    public static final int SQUARES_ACROSS = 300;
    public static final int HELP_COUNT = 1000;
    public static final int HELP_RADIUS = SQUARES_ACROSS / 30;
    public static final double PEN_SPEED = SQUARES_ACROSS / 600.0;
    public static final int CAGE_SIZE = SQUARES_ACROSS / 15;
    private static int squareSize;
    private Color[][] grid;
    private Random random = new Random();
    private double pen_x, pen_y;
    private double pen_x_direction;
    private double pen_y_direction;

    public Main() {
        super("Virulence");
    }

    public static void main(String[] args) throws SlickException, LWJGLException {
        final DisplayMode mode = getDisplayMode();
        squareSize = mode.getWidth() / SQUARES_ACROSS;
        AppGameContainer container = new AppGameContainer(new Main(), mode.getWidth(), mode.getHeight(), true);
        container.setShowFPS(false);
        container.start();
    }

    private static DisplayMode getDisplayMode() throws LWJGLException {
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        int max = 0;
        int max_index = 0;
        for (int i = 0; i < modes.length; ++i) {
            if (modes[i].getWidth() > max) {
                max = modes[i].getWidth();
                max_index = i;
            }
        }
        return modes[max_index];
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        try {
            final DisplayMode mode = getDisplayMode();
            final int width = mode.getWidth();
            final int height = mode.getHeight();
            grid = new Color[height / squareSize + 1][width / squareSize + 1];
        } catch (LWJGLException e) {
            throw new SlickException(e.getMessage(), e);
        }
        pen_x = getWidth() / 2.0;
        pen_y = getHeight() / 2.0;
        Color[] colors = {Color.blue, Color.green.darker(), Color.orange, Color.magenta, Color.red, Color.cyan};
        for (int i = 0; i < colors.length; ++i)
            grid[random.nextInt(getHeight())][random.nextInt(getWidth())] = colors[i % colors.length];
    }

    @Override
    public void update(GameContainer gameContainer, int seconds) {
        pen_x += pen_x_direction;
        pen_y += pen_y_direction;
        int pen_column = (int) Math.round(pen_x);
        int pen_row = (int) Math.round(pen_y);
        if (pen_column >= getWidth())
            pen_column = getWidth() - 1;
        if (pen_column < 0)
            pen_column = 0;
        if (pen_row >= getHeight())
            pen_row = getHeight() - 1;
        if (pen_row < 0)
            pen_row = 0;
        grid[pen_row][pen_column] = Color.white;

        final int max = getHeight() * getWidth() * 2;
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            int other_x = x + random.nextInt(3) - 1;
            int other_y = y + random.nextInt(3) - 1;
            if ((other_x != x || other_y != y) && isInsideGrid(other_x, other_y)) {
                final Color other = grid[other_y][other_x];
                if (other != null && other != Color.white && grid[y][x] != Color.white)
                    grid[y][x] = other;
            }
        }
    }

    private boolean isInsideGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {
        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++) {
                final Color color = grid[y][x];
                if (color != null) {
                    g.setColor(color);
                    g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                }
            }
        float brightness = random.nextFloat();
        g.setColor(new Color(brightness, brightness, brightness));
        g.fillRect(Math.round(pen_x) * squareSize, Math.round(pen_y) * squareSize, squareSize, squareSize);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        final int row = y / squareSize;
        final int column = x / squareSize;
        if (grid[row][column] == null)
            return;

        if (button == Input.MOUSE_LEFT_BUTTON) {
            blastThroughWalls(row, column);
        } else {
            buildCage(row, column);
        }
    }

    private void blastThroughWalls(int row, int column) {
        for (int i = 0; i < HELP_COUNT; i++) {
            int other_row = row + random.nextInt(HELP_RADIUS) - HELP_RADIUS / 2;
            if (other_row < 0 || other_row >= getHeight())
                continue;
            int other_column = column + random.nextInt(HELP_RADIUS) - HELP_RADIUS / 2;
            if (other_column < 0 || other_column >= getWidth())
                continue;
            if (grid[other_row][other_column] == Color.white)
                grid[other_row][other_column] = null;
        }
    }

    private void buildCage(int row, int column) {
        int x1 = limits(column - CAGE_SIZE / 2, getWidth());
        int x2 = limits(column + CAGE_SIZE / 2, getWidth());
        int y1 = limits(row - CAGE_SIZE / 2, getHeight());
        int y2 = limits(row + CAGE_SIZE / 2, getHeight());
        drawLine(x1, y1, x2, y1);
        drawLine(x1, y2, x2, y2);
        drawLine(x1, y1, x1, y2);
        drawLine(x2, y1, x2, y2);
        for (int yy = y1 + 1; yy < y2; ++yy) {
            for (int xx = x1 + 1; xx < x2; ++xx) {
                if (grid[yy][xx] == Color.white) {
                    grid[yy][xx] = null;
                }
            }
        }
    }

    private int getHeight() {
        return grid.length;
    }

    private int getWidth() {
        return grid[0].length;
    }

    private int limits(int a, int maximum) {
        return Math.min(Math.max(a, 0), maximum - 1);
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        if (y1 == y2) {
            for (int x = x1; x <= x2; ++x)
                grid[y1][x] = Color.white;
        } else {
            for (int y = y1; y <= y2; ++y)
                grid[y][x1] = Color.white;
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_DOWN:
                pen_y_direction = PEN_SPEED;
                break;
            case Input.KEY_UP:
                pen_y_direction = -PEN_SPEED;
                break;
            case Input.KEY_LEFT:
                pen_x_direction = -PEN_SPEED;
                break;
            case Input.KEY_RIGHT:
                pen_x_direction = PEN_SPEED;
                break;
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        pen_x_direction = pen_y_direction = 0;
    }
}
