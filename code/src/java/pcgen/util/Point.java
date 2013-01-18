package pcgen.util;

/**
 * This class helps us remove dependency on AWT's Point2D
 */
public class Point {
	private final double x;
	private final double y;

	public Point(double x, double y) {
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
