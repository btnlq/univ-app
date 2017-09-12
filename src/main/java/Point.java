class Point {

  private double x;
  private double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double distance(Point other) {
    return Math.hypot(x - other.x, y - other.y);
  }

  public void append(Point other) {
    x += other.x;
    y += other.y;
  }

  public Point multiply(double factor) {
    return new Point(x * factor, y * factor);
  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
