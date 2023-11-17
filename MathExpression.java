import com.fathzer.soft.javaluator.DoubleEvaluator;

public class MathExpression {
    private String stringExpression;
    private double value; // stores value of this expression

    public MathExpression(String stringExpression) {
        this.stringExpression = stringExpression;
        this.value = evaluate();
    }

    /** evaluate the value of the function
     * set this.value to that value
     * */
    private double evaluate() {
        DoubleEvaluator evaluator = new DoubleEvaluator();
        return evaluator.evaluate(this.stringExpression);
    }

    /**
     * Gets value of this expression
     * @return Value of this expression
     */
    public double getValue() { return this.value; }

    public static void main(String[] args) {
        MathExpression mex = new MathExpression("5+6-(6*7)");
        System.out.println(mex.evaluate());
    }

}
