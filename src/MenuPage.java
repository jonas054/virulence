import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.stream.Collectors;

public class MenuPage extends Page {
    private int line;

    public MenuPage(Main main) {
        super(main);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        DisplayMode displayMode;
        try {
            displayMode = Main.getDisplayMode();
        } catch (LWJGLException e) {
            throw new SlickException("Failed", e);
        }
        g.setColor(Color.white);
        g.fillRect(0, 0, displayMode.getWidth(), displayMode.getHeight());
        g.setColor(Color.blue);
        line = 0;

        write(g, "Virulence");
        write(g, "=========");
        write(g, "Control the spreading of colors!");
        write(g, "");
        write(g, "Pick a color. Make this color fill the whole screen.");
        write(g, "Use arrow keys to draw walls.");
        write(g, "Right-click to build a cage with four walls.");
        write(g, "Hold down the left mouse button to erase walls. Also useful to fight the spreading of other colors.");
        write(g, "Left-shift on the keyboard can be used as an alternative to the left mouse button.");
        write(g, "");
        write(g, "Click mouse to start.");
    }

    private void write(Graphics g, String text) {
        g.drawString(text, 100, 100 + 20 * line++);
    }

    @Override
    public boolean closeRequested() {
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void keyPressed(int i, char c) {

    }

    @Override
    public void keyReleased(int i, char c) {

    }

    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mousePressed(int i, int i1, int i2) {
        getMain().startGame();
    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {

    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3) {

    }
}
