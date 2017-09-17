package clustering;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

  private Point center;
  private final List<Point> points = new ArrayList<>();

  Cluster(Point center) {
    this.center = center;
  }

  public Point getCenter() {
    return center;
  }

  double distance(Point point) {
    return center.distance(point);
  }

  void addPoint(Point point) {
    points.add(point);
  }

  void clearPoints() {
    points.clear();
  }

  double updateCenter() {

    double x = 0;
    double y = 0;

    for (Point point : points) {
      x += point.getX();
      y += point.getY();
    }
    int cnt = points.size();
    Point newCenter = new Point(x / cnt, y / cnt);

    double distance = center.distance(newCenter);
    center = newCenter;
    return distance;
  }

  /**
   * Вычисляет стандартное отклонение точек, принадлежащих кластеру.
   * @return стандартное отклонение точек, принадлежащих кластеру
   */
  public double deviation() {
    if (points.isEmpty()) {
      return 0;
    } else {
      double sum = 0;
      for (Point point : points) {
        sum += center.distanceSq(point);
      }
      return Math.sqrt(sum / points.size());
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Cluster ").append(center).append(": ").append(deviation());
    for (Point point : points) {
      sb.append('\n').append(point);
    }
    return sb.toString();
  }
}
