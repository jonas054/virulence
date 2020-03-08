import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class MenuPage extends Page {
  private int line;

  public MenuPage(Main main) {
    super(main);
  }

  @Override
  public void init(GameContainer gameContainer) {}

  @Override
  public void update(GameContainer gameContainer, int i) {}

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
    write(g, "Pick a color. Build walls around this color, expand the walls,");
    write(g, "and make the color fill the whole screen.");
    write(g, "Left-click and drag the mouse to build a cage with four walls.");
    write(g, "Hold down the left shift key on the keyboard and move the mouse");
    write(g, "to erase walls. Also useful for fighting (erasing) other colors.");
    write(g, "Click mouse to start, then Esc to pause the game.");
  }

  private void write(Graphics g, String text) {
    g.drawString(text, 50, 50 + 30 * line++);
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
