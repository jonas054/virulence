import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

class Eraser {
    private final int eraseRadius;
    private int x = -1;
    private int y = -1;

    Eraser(int eraseRadius) {
        this.eraseRadius = eraseRadius;
    }

    void draw(Graphics g, int squareSize) {
        if (x != -1) {
            final int radiusInPixels = eraseRadius * squareSize;
            g.setColor(Color.white);
            g.fillRect(x - radiusInPixels, y - radiusInPixels, 2 * radiusInPixels, 2 * radiusInPixels);
        }
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void hide() {
        x = y = -1;
    }

    boolean isVisible() {
        return x != -1;
    }
}
