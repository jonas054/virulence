import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

class Eraser {
    private static final int HIDDEN = -1;
    private final int eraseRadius;
    private int x = HIDDEN;
    private int y = HIDDEN;

    Eraser(int eraseRadius) {
        this.eraseRadius = eraseRadius;
    }

    void draw(Graphics g, int squareSize) {
        if (isVisible()) {
            final int radiusInPixels = eraseRadius * squareSize;
            g.setColor(Color.white);
            g.fillRect(x - radiusInPixels, y - radiusInPixels,
                       2 * radiusInPixels, 2 * radiusInPixels);
        }
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void hide() {
        x = y = HIDDEN;
    }

    boolean isVisible() {
        return x != HIDDEN;
    }
}
