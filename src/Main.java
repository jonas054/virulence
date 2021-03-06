import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Main extends BasicGame {
    private final GamePage gamePage;
    private final MenuPage menuPage;
    private Page currentPage;

    public Main(int windowWidth, int windowHeight) {
        super("Virulence");
        currentPage = menuPage = new MenuPage(this);
        gamePage = new GamePage(this, windowWidth, windowHeight);
    }

    public static void main(String[] args) throws SlickException, LWJGLException {
        final DisplayMode mode = getDisplayMode();
        final int windowWidth = mode.getWidth() - 100;
        final int windowHeight = mode.getHeight() - 100;
        AppGameContainer container = new AppGameContainer(new Main(windowWidth, windowHeight),
                                                          windowWidth, windowHeight, false);
        container.setShowFPS(false);
        container.start();
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        currentPage.init(gameContainer);
        gamePage.init(gameContainer);
    }

    static DisplayMode getDisplayMode() throws LWJGLException {
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        final int IDEAL = 2000;
        int min_diff = 1000000;
        int best_index = 0;
        for (int i = 0; i < modes.length; ++i) {
            final int diff = Math.abs(modes[i].getWidth() - IDEAL);
            if (diff < min_diff) {
                min_diff = diff;
                best_index = i;
            }
        }
        return modes[best_index];
    }

    @Override
    public void update(GameContainer gameContainer, int seconds) throws SlickException {
        currentPage.update(gameContainer, seconds);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        currentPage.render(gameContainer, g);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        currentPage.mousePressed(button, x, y);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        currentPage.mouseDragged(oldx, oldy, newx, newy);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        currentPage.mouseMoved(oldx, oldy, newx, newy);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        currentPage.mouseReleased(button, x, y);
    }

    @Override
    public void keyPressed(int key, char c) {
        currentPage.keyPressed(key, c);
    }

    @Override
    public void keyReleased(int key, char c) {
        currentPage.keyReleased(key, c);
    }

    static int limits(int a, int maximum) {
        return Math.min(Math.max(a, 0), maximum - 1);
    }

    public void startGame() {
        currentPage = gamePage;
    }

    public void stopGame() {
        currentPage = menuPage;
    }
}
