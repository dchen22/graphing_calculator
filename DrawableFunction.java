import javafx.scene.paint.Color;

public class DrawableFunction {
    private String funcExpression;
    private Color color;
    public DrawableFunction(String funcExpression, Color color) {
        this.funcExpression = funcExpression;
        this.color = color;
    }
    public String getExpr() { return this.funcExpression; }

    public Color getColor() { return this.color; }
}
