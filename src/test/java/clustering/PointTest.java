package clustering;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTest {

  private static final double DELTA = 1e-6;

  @Test
  public void testDistanceSq() {
    Point point1 = new Point(4, 2);
    Point point2 = new Point(7, 6);

    assertEquals(25, point1.distanceSq(point2), DELTA);
    assertEquals(25, point2.distanceSq(point1), DELTA);
    assertEquals(0, point1.distanceSq(point1), DELTA);
  }

  @Test
  public void testToString() {
    Point point = new Point(7, 6);
    assertEquals("(7.0, 6.0)", point.toString());
  }

}