import java.util.Random;

public class Clustering {

  private static int nearestPoint(Point point, Point[] points) {
    double bestDistance = Float.POSITIVE_INFINITY;
    int bestIndex = -1;

    for (int i = 0; i < points.length; i++) {
      double distance = points[i].distance(point);

      if (distance < bestDistance) {
        bestDistance = distance;
        bestIndex = i;
      }
    }

    return bestIndex;
  }

  static class Centroid {

    private double x;
    private double y;
    private int cnt;

    void putPoint(Point point) {
      x += point.getX();
      y += point.getY();
      cnt++;
    }

    Point centroid() {
      return new Point(x/cnt, y/cnt);
    }
  }

  /**
   * Обновляет положение центров кластеров в соответствии с алгоритмом kMeans.
   *
   * @param clusters Положения центров кластеров
   * @param points Кластеризуемые точки
   * @return Массив из номеров кластеров, в которые попадают соответствующие точки
   */
  public static int[] kMeans(Point[] clusters, Point[] points) {

    int[] clustersIndex = new int[points.length];

    while (true) {
      boolean changed = false;
      for (int i = 0; i < points.length; i++) {
        int newIndex = nearestPoint(points[i], clusters);
        if (clustersIndex[i] != newIndex) {
          changed = true;
          clustersIndex[i] = newIndex;
        }
      }
      if (!changed) {
        break;
      }

      Centroid[] centroids = new Centroid[clusters.length];
      for (int i = 0; i < clusters.length; i++) {
        centroids[i] = new Centroid();
      }
      for (int i = 0; i < points.length; i++) {
        centroids[clustersIndex[i]].putPoint(points[i]);
      }
      for (int i = 0; i < clusters.length; i++) {
        clusters[i] = centroids[i].centroid();
      }
    }

    return clustersIndex;
  }

  /**
   * Вычисляет положение центров кластеров для заданного набора точек в соответствии с алгоритмом
   * kMeans++.
   *
   * @param points Кластеризуемые точки
   * @param clustersCount Требуемое количество кластеров
   * @return Положеня центров кластеров
   */
  public static Point[] kMeansPP(Point[] points, int clustersCount) {

    Random random = new Random();

    // квадраты расстояний от точки до ближайшего кластера
    double[] distancesSq = new double[points.length];
    for (int i = 0; i < points.length; i++) {
      distancesSq[i] = Double.POSITIVE_INFINITY;
    }

    Point[] clusters = new Point[clustersCount];
    clusters[0] = points[random.nextInt(points.length)];

    for (int clusterId = 1; clusterId < clustersCount; clusterId++) {

      Point previousCluster = clusters[clusterId - 1];

      // обновляем расстояния с учетом нового добавленного кластера
      double sum = 0;
      for (int i = 0; i < points.length; i++) {
        double distanceSq = Math.min(previousCluster.distanceSq(points[i]), distancesSq[i]);
        distancesSq[i] = distanceSq;
        sum += distanceSq;
      }

      // выбираем новый кластер пропорционально вычисленным расстояниям
      double level = random.nextDouble() * sum;
      sum = 0;
      int i = 0;
      for (; i < points.length; i++) {
        sum += distancesSq[i];
        if (sum >= level) {
          break;
        }
      }

      clusters[clusterId] = points[i];
    }

    return clusters;
  }
}


