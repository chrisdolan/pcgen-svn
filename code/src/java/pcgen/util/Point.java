package pcgen.util;

/**
 * This class helps us remove dependency on AWT's Point2D
 */
public class Point {
	public final int x;
	public final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
