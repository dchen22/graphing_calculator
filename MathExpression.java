import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

public class MathExpression {
    private String stringExpression;
    private DoubleEvaluator evaluator = new DoubleEvaluator(); // evaluator for this expression
    final StaticVariableSet<Double> variables = new StaticVariableSet<Double>(); // stores static math variables
    private double value; // stores value of this expression

    public MathExpression(String stringExpression) {
        this.stringExpression = stringExpression;
        this.value = evaluate();
    }
    public MathExpression(String stringExpression, String vars) {
        this.stringExpression = stringExpression;
        vars = vars.replace("\\s+",""); // get rid of spaces
        String[] varList = vars.split("[=,]"); // left with an array [var1, val1, var2, val2, ... ]
        for (int i = 0; i < varList.length - 1; i+=2) {
            String var_i_stringValue = varList[i+1]; // corresponding value for variable i
            if (var_i_stringValue.isEmpty()) {
                var_i_stringValue = "0"; // default to 0
            }
            this.variables.set(varList[i], Double.parseDouble(var_i_stringValue));
        }

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
        MathExpression mex = new MathExpression("5+6-(sin(3*x)+7*y)", "x=2,y=7");
        System.out.println(mex.getValue());
    }

}
