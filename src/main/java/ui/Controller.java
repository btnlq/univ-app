package ui;

import clustering.Cluster;
import clustering.Clustering;
import clustering.Point;
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

  private final List<Point> points = new ArrayList<>();
  private Cluster[] clusters;

  private static final int POINT_RADIUS = 2;
  private static final int CLUSTER_RADIUS = 3;

  /**
   * Вызывается при запуске окна.
   */
  public void initialize() {
    clustersCountSpinner.valueProperty().addListener((obs, oldValue, newValue)
        -> initButton.setDisable(newValue == 0));
    clear();
  }

  private void redrawClusters() {
    GraphicsContext gc = clustersCanvas.getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, clustersCanvas.getWidth(), clustersCanvas.getHeight());

    gc.setFill(Color.YELLOW);
    for (Cluster cluster : clusters) {
      double derivation = cluster.deviation();
      if (derivation > 0) {
        drawCircle(gc, cluster.getCenter(), derivation);
      }
    }

    gc.setFill(Color.RED);
    for (Cluster cluster : clusters) {
      drawCircle(gc, cluster.getCenter(), CLUSTER_RADIUS);
    }
  }

  /**
   * Вызывается при нажатии на кнопку "Инициализация".
   * Выбрать начальное положение центров кластеров в соответствии с алгоритмом k-means++.
   */
  public void init() {
    clusters = Clustering.kMeansPP(getPoints(), clustersCountSpinner.getValue());
    redrawClusters();

    clusterButton.setDisable(false);
  }

  /**
   * Вызывается при нажатии на кнопку "Кластер".
   * Найти оптимальное положение центров кластеров в соответствии с алгоритмом k-means.
   */
  public void cluster() {
    Clustering.kMeans(clusters, getPoints());
    redrawClusters();
  }

  private Point[] getPoints() {
    return points.toArray(new Point[points.size()]);
  }

  /**
   * Вызывается при нажатии на кнопку "Очистить".
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

  /**
   * Вызывается при клике на canvas. Сохраняет место клика и рисует точку.
   *
   * @param mouseEvent -
   */
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

  private static void drawCircle(GraphicsContext gc, Point point, double radius) {
    gc.fillOval(point.getX() - radius, point.getY() - radius, 2 * radius + 1, 2 * radius + 1);
  }
}
