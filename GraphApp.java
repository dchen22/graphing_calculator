import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GraphApp extends Application {

    private static String mathFunction;
    private DoubleEvaluator evaluator = new DoubleEvaluator(); // evaluator for this expression
    final StaticVariableSet<Double> variables = new StaticVariableSet<Double>(); // stores static math variables

    public GraphApp() {
//        this.mathFunction = mathFunction;
    }

    public static void main(String[] args) {
        mathFunction = "x";
        launch(args);
    }

    public void launchApp(String[] args) {
        // Call the launch method on this instance
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graph Application");

        // Create a Canvas to draw on
        Canvas canvas = new Canvas(400, 400); // Set initial size
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set up the scene
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        // Set up the stage
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();

        // Add a listener to handle window resizing
        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> resizeCanvas(canvas, newWidth.doubleValue(), primaryStage.getHeight()));
        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> resizeCanvas(canvas, primaryStage.getWidth(), newHeight.doubleValue()));

        // Initially draw the graph
        drawGraph(gc, canvas.getWidth(), canvas.getHeight());
    }

    private void resizeCanvas(Canvas canvas, double width, double height) {
        // Set the size of the canvas
        canvas.setWidth(width);
        canvas.setHeight(height);

        // Redraw the graph with the new size
        drawGraph(canvas.getGraphicsContext2D(), width, height);
    }

    private void drawGraph(GraphicsContext gc, double width, double height) {
        // Clear the canvas
        gc.clearRect(0, 0, width, height);

        // Set the origin to the center of the canvas
        double centerX = width / 2;
        double centerY = height / 2;

        // Draw X and Y axes
        gc.strokeLine(0, centerY, width, centerY); // X-axis
        gc.strokeLine(centerX, 0, centerX, height); // Y-axis

        // Example points for testing
        double[] xPoints = {1, -2, 3, -4, 5};
        double[] yPoints = {2, -3, 4, -5, 6};

        // Set the color of the points
        gc.setFill(javafx.scene.paint.Color.BLUE);

        // Draw points
        int SCALE_FACTOR = 50;
        for (int i = (int)(-width); i < (int)(width); i++) {
            double x_val = (double)i / SCALE_FACTOR; // value of i scaled for better visibility
            variables.set("x", x_val); // iterate over values along width of graph

            // offset to origin
            double x = centerX + (double)i;
            double y = centerY - evaluator.evaluate(mathFunction, variables) * SCALE_FACTOR; // Invert y for proper coordinate system
//            System.out.println((x_val) + " " + (y-centerY));

            double pointWidth = 6.0;
            gc.fillOval(x - pointWidth / 2, y - pointWidth / 2, pointWidth, pointWidth); // Draw a small circle for each point
        }
    }
}
