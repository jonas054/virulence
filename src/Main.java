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
    public static final int ERASE_RADIUS = SQUARES_ACROSS / 100;
    public static final double PEN_SPEED = SQUARES_ACROSS / 600.0;
    public static final int CAGE_SIZE = SQUARES_ACROSS / 15;
    private static int squareSize;
    private Screen screen;
    private Random random = new Random();
    private Pen pen;
    private Eraser eraser = new Eraser(ERASE_RADIUS);
    private boolean leftShiftKeyIsDown;

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

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        try {
            final DisplayMode mode = getDisplayMode();
            screen = new Screen(mode.getWidth(), mode.getHeight(), squareSize);
        } catch (LWJGLException e) {
            throw new SlickException(e.getMessage(), e);
        }
        pen = new Pen(PEN_SPEED, screen.getWidth(), screen.getHeight(), screen);
        placeInitialColoredDots();
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

    private void placeInitialColoredDots() {
        Color[] colors = {Color.blue, Color.green.darker(), Color.orange, Color.magenta, Color.red, Color.cyan};
        for (int i = 0; i < colors.length; ++i)
            screen.set(random.nextInt(screen.getHeight()), random.nextInt(screen.getWidth()), colors[i % colors.length]);
    }

    @Override
    public void update(GameContainer gameContainer, int seconds) {
        pen.move();
        addRandomDotsOfCopiedColors();
    }

    private void addRandomDotsOfCopiedColors() {
        final int max = screen.getHeight() * screen.getWidth();
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(screen.getWidth());
            int y = random.nextInt(screen.getHeight());
            int other_x = x + random.nextInt(3) - 1;
            int other_y = y + random.nextInt(3) - 1;
            if ((other_x != x || other_y != y) && isInsideGrid(other_x, other_y)) {
                final Color other = screen.get(other_x, other_y);
                if (other != null && other != Color.white && screen.get(x, y) != Color.white)
                    screen.set(y, x, other);
            }
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {
        drawGrid(g);
        pen.flicker(g, squareSize);
        eraser.draw(g, squareSize);
    }

    private void drawGrid(Graphics g) {
        for (int y = 0; y < screen.getHeight(); y++)
            for (int x = 0; x < screen.getWidth(); x++) {
                final Color color = screen.get(x, y);
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

        if (button == Input.MOUSE_LEFT_BUTTON) {
            eraser.setPosition(x, y);
            erase(row, column);
        } else {
            eraser.hide();
            buildCage(row, column);
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if (eraser.isHidden())
            return;

        eraser.setPosition(newx, newy);
        erase(oldy / squareSize, oldx / squareSize);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (leftShiftKeyIsDown) {
            eraser.setPosition(newx, newy);
            mouseDragged(oldx, oldy, newx, newy);
        }
    }

    private void erase(int row, int column) {
        final int radius = ERASE_RADIUS;
        int x1 = limits(column - radius, screen.getWidth());
        int x2 = limits(column + radius, screen.getWidth());
        int y1 = limits(row - radius, screen.getHeight());
        int y2 = limits(row + radius, screen.getHeight());
        for (int yy = y1; yy <= y2; ++yy) {
            for (int xx = x1; xx <= x2; ++xx) {
                screen.set(yy, xx, null);
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        eraser.hide();
    }

    private void buildCage(int row, int column) {
        final int radius = CAGE_SIZE / 2;
        int x1 = limits(column - radius, screen.getWidth());
        int x2 = limits(column + radius, screen.getWidth());
        int y1 = limits(row - radius, screen.getHeight());
        int y2 = limits(row + radius, screen.getHeight());
        screen.drawFourWalls(x1, x2, y1, y2);
        screen.removeWalls(x1, x2, y1, y2);
    }

    private boolean isInsideGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < screen.getWidth() && y < screen.getHeight();
    }

    private int limits(int a, int maximum) {
        return Math.min(Math.max(a, 0), maximum - 1);
    }

    @Override
    public void keyPressed(int key, char c) {
        pen.keyPressed(key);
        if (key == Input.KEY_LSHIFT)
            leftShiftKeyIsDown = true;
    }

    @Override
    public void keyReleased(int key, char c) {
        pen.keyReleased();
        if (key == Input.KEY_LSHIFT) {
            leftShiftKeyIsDown = false;
            eraser.hide();
        }
    }
}
