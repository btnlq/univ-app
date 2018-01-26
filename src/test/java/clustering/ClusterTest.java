package clustering;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClusterTest {

  private static final double DELTA = 1e-6;

  private Cluster emptyCluster;
  private Cluster onePointCluster;
  private Cluster squareCluster;

  @Before
  public void setUp() {
    emptyCluster = new Cluster(new Point(6, -9));

    onePointCluster = new Cluster(new Point(0, 0));
    onePointCluster.addPoint(new Point(1, 1));

    squareCluster = new Cluster(new Point(3, 4));
    squareCluster.addPoint(new Point(7, 7));
    squareCluster.addPoint(new Point(7, -7));
    squareCluster.addPoint(new Point(-7, 7));
    squareCluster.addPoint(new Point(-7, -7));

    // allClusters = new Cluster[]{emptyCluster, onePointCluster, squareCluster};
  }

  @Test
  public void testGetCenter() {
    assertEquals(new Point(6, -9), emptyCluster.getCenter());
    assertEquals(new Point(0, 0), onePointCluster.getCenter());
    assertEquals(new Point(3, 4), squareCluster.getCenter());
  }

  @Test
  public void testDistance() {
    assertEquals(Math.sqrt(23 * 23 + 15 * 15), emptyCluster.distance(new Point(-17, 6)), DELTA);
    assertEquals(0, onePointCluster.distance(new Point(0, 0)), DELTA);
    assertEquals(5, squareCluster.distance(new Point(0, 0)), DELTA);
  }

  @Test
  public void testUpdateCenter() {
    assertEquals(0, emptyCluster.updateCenter(), DELTA);

    assertEquals(Math.sqrt(2), onePointCluster.updateCenter(), DELTA);
    assertEquals(new Point(1, 1), onePointCluster.getCenter());

    assertEquals(5, squareCluster.updateCenter(), DELTA);
    assertEquals(new Point(0, 0), squareCluster.getCenter());
  }

  @Test
  public void testDeviation() {
    assertEquals(0, emptyCluster.deviation(), DELTA);
    assertEquals(Math.sqrt(2), onePointCluster.deviation(), DELTA);
    squareCluster.updateCenter();
    assertEquals(7 * Math.sqrt(2), squareCluster.deviation(), DELTA);
  }

  @Test
  public void testClearPoints() {
    for (Cluster cluster : new Cluster[]{emptyCluster, onePointCluster, squareCluster}) {
      cluster.clearPoints();
      assertEquals(0, cluster.size());
    }
  }
}