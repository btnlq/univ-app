package ui;

import clustering.Clustering;
import clustering.Point;
import java.util.Random;

public class Main {

  private static Point[] generateRandomPoints(int count, int height, int width) {
    Random random = new Random();

    Point[] points = new Point[count];
    for (int i = 0; i < count; i++) {
      points[i] = new Point(random.nextInt(width), random.nextInt(height));
    }

    return points;
  }

  /**
   * Запускает kMeans++ на случайным образом сгенерированных данных.
   * @param args ignored
   */
  public static void main(String[] args) {

    int height = 100;
    int width = 100;

    Point[] points = generateRandomPoints(10, height, width);
    Point[] clusters = Clustering.kMeansPP(points, 3);
    int[] clustersIndex = Clustering.kMeans(clusters, points);

    for (int clusterIndex = 0; clusterIndex < clusters.length; clusterIndex++) {
      System.out.println("Cluster " + clusters[clusterIndex]);
      for (int pointIndex = 0; pointIndex < points.length; pointIndex++) {
        if (clustersIndex[pointIndex] == clusterIndex) {
          System.out.println(points[pointIndex]);
        }
      }
      System.out.println();
    }
  }
}
