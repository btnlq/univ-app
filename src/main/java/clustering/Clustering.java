package clustering;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public final class Clustering {

  private Clustering() {
  }

  private static Cluster nearestCluster(Point point, Cluster[] clusters) {
    return Arrays.stream(clusters).min(Comparator.comparing(x -> x.distance(point))).get();
  }

  /**
   * Обновляет положение центров кластеров в соответствии с алгоритмом kMeans.
   *
   * @param clusters Положения центров кластеров
   * @param points Кластеризуемые точки
   */
  public static void kMeans(Cluster[] clusters, Point[] points) {

    while (true) {

      for (Cluster cluster : clusters) {
        cluster.clearPoints();
      }

      for (Point point : points) {
        Cluster cluster = nearestCluster(point, clusters);
        cluster.addPoint(point);
      }

      if (Arrays.stream(clusters).mapToDouble(Cluster::updateCenter).sum() < 1e-6) {
        break;
      }
    }
  }

  /**
   * Вычисляет положение центров кластеров для заданного набора точек в соответствии с алгоритмом
   * kMeans++.
   *
   * @param points Кластеризуемые точки
   * @param clustersCount Требуемое количество кластеров
   * @return Положения центров кластеров
   */
  public static Cluster[] kMeansPP(Point[] points, int clustersCount) {

    Random random = new Random();

    // квадраты расстояний от точки до ближайшего кластера
    double[] distances = new double[points.length];
    for (int i = 0; i < points.length; i++) {
      distances[i] = Double.POSITIVE_INFINITY;
    }

    Cluster[] clusters = new Cluster[clustersCount];
    clusters[0] = new Cluster(points[random.nextInt(points.length)]);

    for (int clusterId = 1; clusterId < clustersCount; clusterId++) {

      Cluster previousCluster = clusters[clusterId - 1];

      // обновляем расстояния с учетом нового добавленного кластера
      double sum = 0;
      for (int i = 0; i < points.length; i++) {
        double distance = Math.min(previousCluster.distance(points[i]), distances[i]);
        distances[i] = distance;
        sum += distance;
      }

      // выбираем новый кластер пропорционально вычисленным расстояниям
      double level = random.nextDouble() * sum;
      sum = 0;
      int i = 0;
      for (; i < points.length; i++) {
        sum += distances[i];
        if (sum >= level) {
          break;
        }
      }

      clusters[clusterId] = new Cluster(points[i]);
    }

    return clusters;
  }
}
