import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Eraser {
    private final int eraseRadius;
    private int x = -1;
    private int y = -1;

    public Eraser(int eraseRadius) {
        this.eraseRadius = eraseRadius;
    }

    public void draw(Graphics g, int squareSize) {
        if (x != -1) {
            final int radiusInPixels = eraseRadius * squareSize;
            g.setColor(Color.white);
            g.fillRect(x - radiusInPixels, y - radiusInPixels, 2 * radiusInPixels, 2 * radiusInPixels);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void hide() {
        x = y = -1;
    }

    public boolean isHidden() {
        return x == -1;
    }
}
