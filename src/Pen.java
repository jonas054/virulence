import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.Random;

public class Pen {
    private final Screen screen;
    private double x, y;
    private double xDirection = 0.0;
    private double yDirection = 0.0;
    private final double speed;
    private final int width;
    private final int height;
    private final Random random = new Random();

    public Pen(double speed, int width, int height, Screen screen) {
        this.x = width / 2.0;
        this.y = height / 2.0;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.screen = screen;
    }

    public void move() {
        x += xDirection;
        y += yDirection;
        screen.set(getRow(), getColumn(), Color.white);
    }

    public void keyPressed(int key) {
        switch (key) {
            case Input.KEY_DOWN:
                setYDirection(1);
                break;
            case Input.KEY_UP:
                setYDirection(-1);
                break;
            case Input.KEY_LEFT:
                setXDirection(-1);
                break;
            case Input.KEY_RIGHT:
                setXDirection(1);
                break;
        }
    }

    void flicker(Graphics g, int squareSize) {
        float brightness = random.nextFloat();
        g.setColor(new Color(brightness, brightness, brightness));
        g.fillRect(getX() * squareSize, getY() * squareSize, squareSize, squareSize);
    }

    public int getColumn() {
        return limits((int) Math.round(x), height);
    }

    public int getRow() {
        return limits((int) Math.round(y), width);
    }

    private int limits(int a, int maximum) {
        return Math.min(Math.max(a, 0), maximum - 1);
    }

    public int getX() {
        return (int) Math.round(x);
    }

    public int getY() {
        return (int) Math.round(y);
    }

    private void setYDirection(int i) {
        yDirection = i * speed;
    }

    private void setXDirection(int i) {
        xDirection = i * speed;
    }

    public void keyReleased() {
        xDirection = 0;
        yDirection = 0;
    }
}
