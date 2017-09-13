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

  private void redrawClusters() {
    GraphicsContext gc = clustersCanvas.getGraphicsContext2D();
    gc.setFill(Color.WHITE);
    clear(clustersCanvas);
    gc.setFill(Color.RED);
    for(Point point : clusters) {
      drawCircle(gc, point, CLUSTER_RADIUS);
    }
  }

  public void init() {
    clusters = Clustering.kMeansPP(points.toArray(new Point[0]), 3);
    redrawClusters();
  }

  public void cluster() {
    Clustering.kMeans(clusters, points.toArray(new Point[0]));
    redrawClusters();
  }

  public void clear() {
    clear(pointsCanvas);
    clear(clustersCanvas);
    points.clear();
  }

  public void onCanvasClicked(MouseEvent mouseEvent) {
    double x = mouseEvent.getX();
    double y = mouseEvent.getY();
    Point point = new Point(x, y);
    points.add(point);
    drawCircle(pointsCanvas.getGraphicsContext2D(), point, POINT_RADIUS);
  }

  private static void drawCircle(GraphicsContext gc, Point p, int radius) {
    gc.fillOval(p.getX()-radius, p.getY()-radius, 2*radius+1, 2*radius+1);
  }

  private static void clear(Canvas canvas) {
    canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }
}
