package clustering;

import javafx.geometry.Point2D;

public class Point extends Point2D {

  /**
   * Creates a new instance of {@code clustering.Point}.
   *
   * @param x the x coordinate of the point
   * @param y the y coordinate of the point
   */
  public Point(double x, double y) {
    super(x, y);
  }

  double distanceSq(Point other) {
    double dx = getX() - other.getX();
    double dy = getY() - other.getY();
    return dx * dx + dy * dy;
  }

  public String toString() {
    return "(" + getX() + ", " + getY() + ")";
  }
}
