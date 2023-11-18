import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;

public class GraphApp extends Application {
    private Canvas canvas = new Canvas(400, 400); // Set initial size
    private static ArrayList<DrawableFunction> mathFunctions = new ArrayList<>();
    private DoubleEvaluator evaluator = new DoubleEvaluator(); // evaluator for this expression
    final StaticVariableSet<Double> variables = new StaticVariableSet<>(); // stores static math variables
    private double SCALE_FACTOR = 50; // Initial scale factor

    public static void main(String[] args) {
        mathFunctions.add(new DrawableFunction("sin(x)", Color.BLACK));
        mathFunctions.add(new DrawableFunction("x^2", Color.RED));
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

        // Add scroll event handler for zooming
        canvas.setOnScroll(this::handleScroll);

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

    private void handleScroll(ScrollEvent event) {
        // Adjust the SCALE_FACTOR based on the scroll direction
        double d_SCALE = 1.1; // how much SCALE_FACTOR is changed by on scroll
        double delta = event.getDeltaY();
        if (delta > 0) {
            SCALE_FACTOR *= d_SCALE; // Increase the scale factor by dScale
        } else {
            SCALE_FACTOR /= d_SCALE; // Decrease the scale factor by dScale
        }

        // Redraw the graph with the updated scale factor
        drawGraph(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
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

        // Draw points
        for (DrawableFunction df : mathFunctions) {
            // Color of points
            gc.setFill(df.getColor());
            String df_expr = df.getExpr();

            for (int i = (int) (-width); i < (int) (width); i++) {
                double x_val = (double) i / SCALE_FACTOR;
                variables.set("x", x_val);

                double x = centerX + (double) i;
                double y = centerY - evaluator.evaluate(df_expr, variables) * SCALE_FACTOR;
                double pointWidth = 6.0;
                gc.fillOval(x - pointWidth / 2, y - pointWidth / 2, pointWidth, pointWidth);
            }
        }
    }
}
