package pcgen.util;

/**
 * This class exists to remove the dependency on java.awt.Rectangle
 */
public class Rectangle {
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle other) {
        this(other.x, other.y, other.width, other.height);
    }

    // public Rectangle(double x, double y, double width, double height) {
    // this.x = (int) x;
    // this.y = (int) y;
    // this.width = (int) width;
    // this.height = (int) height;
    // }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
