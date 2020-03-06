import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Main extends BasicGame {
    public static final int SQUARES_ACROSS = 300;
    public static final int ERASE_RADIUS = SQUARES_ACROSS / 100;
    public static final double PEN_SPEED = SQUARES_ACROSS / 600.0;
    public static final int CAGE_SIZE = SQUARES_ACROSS / 15;
    private static int squareSize;
    private Screen screen;
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
        screen.placeInitialColoredDots();
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
    public void update(GameContainer gameContainer, int seconds) {
        pen.move();
        screen.addRandomDotsOfCopiedColors();
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {
        screen.paint(g);
        pen.flicker(g);
        eraser.draw(g, squareSize);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        final int row = y / squareSize;
        final int column = x / squareSize;

        if (button == Input.MOUSE_LEFT_BUTTON) {
            eraser.setPosition(x, y);
            screen.erase(row, column, ERASE_RADIUS);
        } else {
            eraser.hide();
            screen.buildCage(row, column, CAGE_SIZE / 2);
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if (eraser.isHidden())
            return;

        eraser.setPosition(newx, newy);
        screen.erase(oldy / squareSize, oldx / squareSize, ERASE_RADIUS);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (leftShiftKeyIsDown) {
            eraser.setPosition(newx, newy);
            mouseDragged(oldx, oldy, newx, newy);
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        eraser.hide();
    }

    static int limits(int a, int maximum) {
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
