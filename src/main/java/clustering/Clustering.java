package clustering;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public final class Clustering {

  private static final double EPS = 1e-6;

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

      if (Arrays.stream(clusters).mapToDouble(Cluster::updateCenter).sum() < EPS) {
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

    // расстояния от точки до ближайшего кластера
    double[] distances = new double[points.length];
    Arrays.fill(distances, Double.POSITIVE_INFINITY);

    Cluster[] clusters = new Cluster[clustersCount];
    clusters[0] = new Cluster(points[random.nextInt(points.length)]);

    for (int clusterId = 1; clusterId < clustersCount; clusterId++) {

      Cluster previousCluster = clusters[clusterId - 1];

      // обновляем расстояния с учетом нового добавленного кластера
      for (int i = 0; i < points.length; i++) {
        distances[i] = Math.min(previousCluster.distance(points[i]), distances[i]);
      }
      // выбираем новый кластер пропорционально вычисленным расстояниям
      int index = randomFromDistribution(distances, random);
      clusters[clusterId] = new Cluster(points[index]);
    }

    return clusters;
  }

  /**
   * Случайным образом выбирает число от 0 до {@code distribution.length()},
   * причем вероятность выбора {@code i} пропорциональна {@code distribution[i]}.
   *
   * @param distribution Массив вероятностей
   * @param random -
   * @return Выбранное число
   */
  private static int randomFromDistribution(double[] distribution, Random random) {
    double sum = 0;
    for (double probability : distribution) {
      sum += probability;
    }

    double level = random.nextDouble() * sum;
    sum = 0;
    int index = 0;
    for (double probability : distribution) {
      sum += probability;
      if (sum >= level) {
        break;
      }
      index++;
    }
    return index;
  }
}
