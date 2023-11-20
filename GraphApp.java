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

import java.sql.SQLOutput;
import java.util.ArrayList;
import TRANSFORMATIONS.*;

public class GraphApp extends Application {
    /**
     * Constants
     */
    double WIDTH = 600; // initial window & canvas width
    double HEIGHT = 600; // initial window & canvas height
    private static final double SQ2 = Math.sqrt(2);
    private static double SCALE_FACTOR = 50; // Initial scale factor
    private static final double d_SCALE = 1.1; // how much SCALE_FACTOR is changed by on scroll

    /**
     * Not Constants
     */

    private Canvas canvas = new Canvas(WIDTH, HEIGHT); // Set initial size
    private static ArrayList<DrawableFunction> mathFunctions = new ArrayList<>();
    private static ArrayList<MathMatrix> transformations = new ArrayList<>();
    private DoubleEvaluator evaluator = new DoubleEvaluator(); // evaluator for this expression
    private static StaticVariableSet<Double> variables = new StaticVariableSet<>(); // stores static math variables



    public static void main(String[] args) {
        mathFunctions.add(new DrawableFunction("sin(x)", Color.BLACK, 3));
        mathFunctions.add(new DrawableFunction("x^2", Color.RED, 3));
        mathFunctions.add(new DrawableFunction("-1/x", Color.BLUE, 3));
        mathFunctions.add(new DrawableFunction("ln(x)", Color.GREEN, 3));
        transformations.add(LinearTransform.transform("rotate pi/4"));
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
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
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

    /**
     * Adjust the SCALE_FACTOR based on the scroll direction
     */
    private void handleScroll(ScrollEvent event) {
        double delta = event.getDeltaY(); // scroll direction
        if (delta > 0) {
            SCALE_FACTOR *= d_SCALE; // Increase the scale factor by dScale
        } else {
            SCALE_FACTOR /= d_SCALE; // Decrease the scale factor by dScale
        }

        // Redraw the graph with the updated scale factor
        drawGraph(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
    }

    private void drawGraph(GraphicsContext gc, double width, double height) {
        double ORIGIN_TO_CORNER = width / 2 * SQ2; // distance from origin to the corners
        // normally, distance of x-axis in either direction is width / 2, but if we rotate the graph by
        // 45 degs then we need the graph to be able to reach the corners

        // Clear the canvas
        gc.clearRect(0, 0, width, height);

        // Set the origin to the center of the canvas
        double centerX = width / 2;
        double centerY = height / 2;

        // Draw X and Y axes
        gc.setStroke(Color.BLACK); // black axes
        gc.strokeLine(0, centerY, width, centerY); // X-axis
        gc.strokeLine(centerX, 0, centerX, height); // Y-axis

        // Draw points
        for (DrawableFunction df : mathFunctions) {
            boolean lastExists = true; // whether previous point is defined
            boolean newExists = true; // whether previous point is defined
            // set some variables so we don't have to keep accessing the DrawableFunction object
            Color df_color = df.getColor();
            String df_expr = df.getExpr();
            double df_lineWidth = df.getLineWidth();

            // Color of points
            gc.setFill(df_color);

            // when drawing lines, we need a previous point (to the left) to be drawn to current point
            // we will start with the x coord immediately outside the range (-width - 1)
            double lastX = -ORIGIN_TO_CORNER- 1;

            // Adjust the lastY calculation to consider scaling factor
            variables.set("x", lastX / SCALE_FACTOR); // set the x variable to our "previous" x
            double lastY;

            // check for functions with limited domains (e.g. ln(x))
            try {
                lastY = evaluator.evaluate(df_expr, variables) * SCALE_FACTOR; // calculate the raw y value (w/o OTO)
            } catch (IllegalArgumentException iae) {
                lastY = 0; // dummy value
                lastExists = false; // ensure we know that this value is invalid
            }
            for (MathMatrix m : transformations) {
                double[] newPoint = LinearTransform.applyPointTransform(new double[]{lastX, lastY}, m);
                lastX = newPoint[0];
                lastY = newPoint[1];
            }

            lastX = centerX + lastX; // OTO
            lastY = centerY - lastY; // OTO

            // multiply by sq2 to reach the corners if graph is rotated
            for (int i = (int) (-ORIGIN_TO_CORNER); i < (int) (ORIGIN_TO_CORNER); i++) {
                double x = i; // x value
                variables.set("x", x / SCALE_FACTOR); // set "x" variable to current x value

                double y;

                // check for functions with limited domains (e.g. ln(x))
                try {
                    y = evaluator.evaluate(df_expr, variables) * SCALE_FACTOR; // raw y value
                } catch (IllegalArgumentException iae) {
                    y = 0; // dummy value
                    newExists = false;
                }
                for (MathMatrix m : transformations) {
                    double[] newPoint = LinearTransform.applyPointTransform(new double[]{x, y}, m);
                    x = newPoint[0];
                    y = newPoint[1];
                }

                x = centerX + x; // displayed x value (OTO)
                y = centerY - y;

                // draw lines
                gc.setStroke(df_color); // color of line
                gc.setLineWidth(df_lineWidth); // width of line
                if (Double.isInfinite(y)) {
                    // here idk
                }
                if (lastExists && newExists) {
                    gc.strokeLine(
                            lastX,
                            UTIL.boundInf(lastY, -ORIGIN_TO_CORNER*2, ORIGIN_TO_CORNER*2),
                            x,
                            UTIL.boundInf(y, -ORIGIN_TO_CORNER*2, ORIGIN_TO_CORNER*2)); // from previous (left) point drawn to current point
                }
                lastX = x; // now, the current x coord becomes the new "previous" x coord
                lastY = y; // current y coord becomes new previous y coord

                lastExists = newExists; // current point because the new "previous" point, so whether or not it exists depends on if current point exists
                newExists = true; // default value at the start of new iteration
                // continue to next iteration of loop with new lastX and lastY
            }
        }
    }

    private void drawLine(double lastX, double lastY, double x, double y) {

    }
}
