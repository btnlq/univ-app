import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {

  public Canvas pointsCanvas;
  public Canvas clustersCanvas;

  private List<Point> points = new ArrayList<>();
  private Point[] clusters;

  private static final int POINT_RADIUS = 2;
  private static final int CLUSTER_RADIUS = 3;

  private void redrawClusters(double[] derivations) {
    GraphicsContext gc = clustersCanvas.getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    clearCanvas(clustersCanvas);
    if (derivations != null) {
      gc.setFill(Color.YELLOW);
      for (int i = 0; i < clusters.length; i++) {
        if (derivations[i] > 0) {
          drawCircle(gc, clusters[i], derivations[i]);
        }
      }
    }
    gc.setFill(Color.RED);
    for (Point point : clusters) {
      drawCircle(gc, point, CLUSTER_RADIUS);
    }
  }

  /**
   * Выбрать начальное положение центров кластеров в соответствии с алгоритмом k-means++.
   */
  public void init() {
    clusters = Clustering.kMeansPP(points.toArray(new Point[0]), 3);
    redrawClusters(null);
  }

  /**
   * Найти оптимальное положение центров кластеров в соответствии с алгоритмом k-means.
   */
  public void cluster() {
    Point[] pointsArray = points.toArray(new Point[0]);
    int[] clustersIndex = Clustering.kMeans(clusters, pointsArray);
    double[] derivations = Clustering.deviations(clusters, pointsArray, clustersIndex);
    redrawClusters(derivations);

    for (int clusterIndex = 0; clusterIndex < clusters.length; clusterIndex++) {
      System.out.println("Cluster " + clusters[clusterIndex] + ": " + derivations[clusterIndex]);
      for (int pointIndex = 0; pointIndex < pointsArray.length; pointIndex++) {
        if (clustersIndex[pointIndex] == clusterIndex) {
          System.out.println(pointsArray[pointIndex]);
        }
      }
      System.out.println();
    }

  }

  /**
   * Удалить все отмеченные пользователем точки и сформированные кластеры.
   */
  public void clear() {
    clearCanvas(pointsCanvas);
    clearCanvas(clustersCanvas);
    points.clear();
  }

  public void onCanvasClicked(MouseEvent mouseEvent) {
    double x = mouseEvent.getX();
    double y = mouseEvent.getY();
    Point point = new Point(x, y);
    points.add(point);
    drawCircle(pointsCanvas.getGraphicsContext2D(), point, POINT_RADIUS);
  }

  private static void drawCircle(GraphicsContext gc, Point p, double radius) {
    gc.fillOval(p.getX() - radius, p.getY() - radius, 2 * radius + 1, 2 * radius + 1);
  }

  private static void clearCanvas(Canvas canvas) {
    canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }
}
