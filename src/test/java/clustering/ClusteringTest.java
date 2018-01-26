package clustering;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;

public class ClusteringTest {

  // 3 группы по 3 точки
  final private Point[] points = {
      new Point(1, 2), new Point(1, 3), new Point(2, 3),
      new Point(21, 12), new Point(21, 13), new Point(22, 13),
      new Point(11, 22), new Point(11, 23), new Point(12, 23),
  };

  @Test
  public void testKMeans() {

    Cluster[] clusters = {
        new Cluster(new Point(5, 5)),
        new Cluster(new Point(15, 5)),
        new Cluster(new Point(5, 15))
    };

    Clustering.kMeans(clusters, points);

    for (Cluster cluster : clusters) {
      assertEquals(3, cluster.size());
      assertTrue(cluster.deviation() < 5);
    }
  }

  private static final int TEST_COUNT = 20;

  @Test
  public void testKMeansPP() {

    int failure = 0;

    for (int t = 0; t < TEST_COUNT; t++) {
      Cluster[] clusters = Clustering.kMeansPP(points, 3);

      boolean bad = Arrays.stream(clusters).mapToInt(
          cluster -> (int) cluster.getCenter().getX() / 10
      ).distinct().count() < 3;

      if (bad) {
        failure++;
      }
    }

    assertTrue(failure + "", failure < TEST_COUNT / 2);

  }

}