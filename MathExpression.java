import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

public class MathExpression {
    private String stringExpression;
    private DoubleEvaluator evaluator = new DoubleEvaluator(); // evaluator for this expression
    final StaticVariableSet<Double> variables = new StaticVariableSet<Double>(); // stores static math variables
    private double value; // stores value of this expression

    public MathExpression(String stringExpression) {
        this.stringExpression = UTIL.b_to_p(stringExpression);
        this.value = evaluate();
    }
    public MathExpression(String stringExpression, String vars) {
        this.stringExpression = UTIL.b_to_p(stringExpression);

        UTIL.evalVarAssign(vars, this.variables);

        this.value = evaluate();
    }



    /** evaluate the value of the function
     * set this.value to that value
     * */
    private double evaluate() {
        double value = 0;
        boolean modified = false; // true iff value was modified, and the default value of 0 was overwritten
        // if we do double value; then the compiler doesn't like that, so we gotta initialize it
        // but we want to know if there is ever a case where the value never gets modified and is instead evaluated as its initial value

        try {
            value = evaluator.evaluate(this.stringExpression, variables);
            modified = true;
        } catch (IllegalArgumentException iae) {
            String eMessage = iae.getMessage(); // error message: "x is not a number"

            // take the first word in the error message (variable name)
            String varName = eMessage.split(" ")[0];

            variables.set(varName, 0.0); // set it to default value of 0
            value = evaluate(); // call our method again
            modified = true;
        }
        if (!modified) {
            System.out.println("!--------------------------------------------------!");
            System.out.println("!--------------- Value not modified ---------------!");
            System.out.println("!--------------------------------------------------!");
        }
        return value;
    }

    /**
     * Gets value of this expression
     * @return Value of this expression
     */
    public double getValue() { return this.value; }

    public static void main(String[] args) {
        MathExpression mex = new MathExpression("5+6-(sin(3*x)+7*yd)", "x=2,yd=7");
        System.out.println(mex.getValue());
    }

}
