import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

import java.awt.Point;

public class GamePage extends Page {
    static final int SQUARES_ACROSS = 275;
    static final int ERASE_RADIUS = SQUARES_ACROSS / 70;

    private Eraser eraser = new Eraser(ERASE_RADIUS);
    private static int squareSize;
    private Screen screen;
    private boolean leftShiftKeyIsDown;
    private List<String> score = new ArrayList<>();
    private int nextScoreUpdate;
    private Point firstPoint;
    private Point secondPoint;

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
        screen.placeInitialColoredDots();
    }

    @Override
    public void update(GameContainer gameContainer, int seconds) {
        screen.addRandomDotsOfCopiedColors();
        if (nextScoreUpdate++ % 20 == 0) {
            score = screen.calculateScore();
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        screen.paint(g);
        if (firstPoint != null && secondPoint != null) {
            screen.drawOutline(g, firstPoint, secondPoint);
        }
        eraser.draw(g, squareSize);
        drawTextWithShadow(g, score, 25, 20);
    }

    private void drawTextWithShadow(Graphics g, List<String> text, int x, int firstY) {
        int y = firstY;
        for (String line : text) {
            g.setColor(Color.black);
            drawShadow(g, x, y, line, 1);
            drawShadow(g, x, y, line, 2);
            g.setColor(Color.white);
            g.drawString(line, x, y);
            y += 20;
        }
    }

    private void drawShadow(Graphics g, int x, int y, String line, int offset) {
        g.drawString(line, x + offset, y + offset);
        g.drawString(line, x, y + offset);
        g.drawString(line, x + offset, y);
    }

    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mousePressed(int button, int x, int y) {
        firstPoint = new Point(x, y);
        secondPoint = new Point(x, y);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if (secondPoint != null)
            secondPoint.move(newx, newy);
        if (eraser.isVisible()) {
            eraser.setPosition(newx, newy);
            screen.erase(oldy / squareSize, oldx / squareSize, ERASE_RADIUS);
        }
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
        final Point p1 = this.firstPoint;
        if (p1 != null) {
            if (secondPoint == null)
                secondPoint = new Point(x, y);
            else
                secondPoint.move(x, y);
            final Point p2 = this.secondPoint;
            final int s = squareSize;
            screen.drawFourWalls(p1.x / s, p2.x / s, p1.y / s, p2.y / s);
        }
        firstPoint = null;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_LSHIFT)
            leftShiftKeyIsDown = true;
        if (key == Input.KEY_ESCAPE)
            getMain().stopGame();
    }

    @Override
    public void keyReleased(int key, char c) {
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
