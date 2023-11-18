import javafx.scene.paint.Color;

public class DrawableFunction {
    private String funcExpression;
    private Color color;
    private double lineWidth;
    public DrawableFunction(String funcExpression, Color color, double lineWidth) {
        this.funcExpression = funcExpression;
        this.color = color;
        this.lineWidth = lineWidth;
    }
    public String getExpr() { return this.funcExpression; }
    public Color getColor() { return this.color; }
    public double getLineWidth() { return this.lineWidth; }

}
