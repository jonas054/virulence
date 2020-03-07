import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GamePage extends Page {
    static final int SQUARES_ACROSS = 300;
    static final double PEN_SPEED = SQUARES_ACROSS / 600.0;
    static final int ERASE_RADIUS = SQUARES_ACROSS / 100;
    static final int CAGE_SIZE = SQUARES_ACROSS / 15;

    private Eraser eraser = new Eraser(ERASE_RADIUS);
    private static int squareSize;
    private Screen screen;
    private Pen pen;
    private boolean leftShiftKeyIsDown;
    private String score = "";

    public GamePage(Main main) {
        super(main);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        try {
            final DisplayMode mode = Main.getDisplayMode();
            squareSize = mode.getWidth() / SQUARES_ACROSS;
            screen = new Screen(mode.getWidth(), mode.getHeight(), squareSize);
        } catch (LWJGLException e) {
            throw new SlickException(e.getMessage(), e);
        }
        pen = new Pen(PEN_SPEED, screen.getWidth(), screen.getHeight(), screen);
        screen.placeInitialColoredDots();
    }

    @Override
    public void update(GameContainer gameContainer, int seconds) {
        pen.move();
        screen.addRandomDotsOfCopiedColors();
        score = screen.calculateScore();
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {
        screen.paint(g);
        pen.flicker(g);
        eraser.draw(g, squareSize);
        g.setColor(Color.black);
        g.fillRect(10, 10, 650, 40);
        g.setColor(Color.white);
        g.drawString(score, 20, 20);
    }

    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

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
        if (key == Input.KEY_ESCAPE)
            getMain().stopGame();
    }

    @Override
    public void keyReleased(int key, char c) {
        pen.keyReleased();
        if (key == Input.KEY_LSHIFT) {
            leftShiftKeyIsDown = false;
            eraser.hide();
        }
    }

    @Override
    public boolean closeRequested() {
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
