import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {

  public Canvas pointsCanvas;
  public Canvas clustersCanvas;
  public Spinner<Integer> clustersCountSpinner;
  public Button initButton;
  public Button clusterButton;
  public Button clearButton;

  private List<Point> points = new ArrayList<>();
  private Point[] clusters;

  private static final int POINT_RADIUS = 2;
  private static final int CLUSTER_RADIUS = 3;

  public void initialize() {
    clustersCountSpinner.valueProperty().addListener((obs, oldValue, newValue)
        -> initButton.setDisable(newValue == 0));
    clear();
  }

  private void redrawClusters(double[] derivations) {
    GraphicsContext gc = clustersCanvas.getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, clustersCanvas.getWidth(), clustersCanvas.getHeight());
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
    clusters = Clustering.kMeansPP(points.toArray(new Point[0]), clustersCountSpinner.getValue());
    redrawClusters(null);

    clusterButton.setDisable(false);
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
    pointsCanvas.getGraphicsContext2D()
        .clearRect(0, 0, pointsCanvas.getWidth(), pointsCanvas.getHeight());

    GraphicsContext gc = clustersCanvas.getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, clustersCanvas.getWidth(), clustersCanvas.getHeight());

    points.clear();

    clustersCountSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0)
    );

    clusterButton.setDisable(true);
    clearButton.setDisable(true);
  }

  public void onCanvasClicked(MouseEvent mouseEvent) {
    double x = mouseEvent.getX();
    double y = mouseEvent.getY();
    Point point = new Point(x, y);
    points.add(point);
    drawCircle(pointsCanvas.getGraphicsContext2D(), point, POINT_RADIUS);

    clustersCountSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(
            0, points.size(), clustersCountSpinner.getValue()
        )
    );

    clearButton.setDisable(false);
  }

  private static void drawCircle(GraphicsContext gc, Point p, double radius) {
    gc.fillOval(p.getX() - radius, p.getY() - radius, 2 * radius + 1, 2 * radius + 1);
  }
}
