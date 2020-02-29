import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class Main extends BasicGame {
    public static final int SQUARES_ACROSS = 200;
    public static final int HELP_COUNT = 1000;
    public static final int HELP_RADIUS = 20;
    private static int squareSize;
    private Color[][] grid;
    private Random random = new Random();

    public Main() {
        super("Virus");
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
    public void init(GameContainer gameContainer) {
        try {
            final DisplayMode mode = getDisplayMode();
            final int width = mode.getWidth();
            final int height = mode.getHeight();
            grid = new Color[height / squareSize + 1][width / squareSize + 1];
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        Color[] colors = {Color.blue, Color.green.darker(), Color.orange, Color.magenta, Color.red, Color.cyan};
        for (int i = 0; i < colors.length; ++i)
            grid[random.nextInt(grid.length)][random.nextInt(grid[0].length)] = colors[i % colors.length];
    }

    @Override
    public void update(GameContainer gameContainer, int seconds) {
        final int max = grid.length * grid[0].length * 2;
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(grid[0].length);
            int y = random.nextInt(grid.length);
            int other_x = x + random.nextInt(3) - 1;
            int other_y = y + random.nextInt(3) - 1;
            if (other_x != x || other_y != y)
                if (isInsideGrid(other_x, other_y) && grid[other_y][other_x] != null)
                    grid[y][x] = grid[other_y][other_x];
        }
    }

    private boolean isInsideGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < grid[0].length && y < grid.length;
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {
        for (int y = 0; y < grid.length; y++)
            for (int x = 0; x < grid[0].length; x++) {
                final Color color = grid[y][x];
                if (color != null) {
                    g.setColor(color);
                    g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                }
            }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        final int row = y / squareSize;
        final int column = x / squareSize;
        final Color color = grid[row][column];
        if (color == null)
            return;

        for (int i = 0; i < HELP_COUNT; i++) {
            int other_row = row + random.nextInt(HELP_RADIUS) - HELP_RADIUS / 2;
            if (other_row < 0 || other_row >= grid.length)
                continue;
            int other_column = column + random.nextInt(HELP_RADIUS) - HELP_RADIUS / 2;
            if (other_column < 0 || other_column >= grid[0].length)
                continue;
            grid[other_row][other_column] = button == 0 ? color.darker() : color.brighter();
        }
    }
}
