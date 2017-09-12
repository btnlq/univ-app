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

    private Point sum = new Point(0, 0);
    private int cnt = 0;

    void putPoint(Point point) {
      sum.append(point);
      cnt++;
    }

    Point centroid() {
      return sum.multiply(1.0 / cnt);
    }
  }

  /**
   * Обновляет положение центров кластеров в соответствии с алгоритмом kMeans.
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
}


