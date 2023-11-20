import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

public class UTIL {
    private static DoubleEvaluator evaluator = new DoubleEvaluator(); // evaluator for this expression
    private static StaticVariableSet<Double> variables = new StaticVariableSet<>(); // stores static math variables


    /**
     * Replace brackets with parentheses
     * @param s Inputted string
     * @return String with brackets replaced by parentheses
     */
    public static String b_to_p(String s) {
        return s.replace('[', '(').replace(']', ')');
    }

    /**
     * Evaluates a string of variable assignments
     * @param variableString A string of variable assignments (e.g. "x=5, y=8, ab=7")
     */
    public static void evalVarAssign(String variableString, StaticVariableSet<Double> vSet) {
        variableString = variableString.replace("\\s+",""); // get rid of spaces
        String[] varList = variableString.split("[=,]"); // left with an array [var1, val1, var2, val2, ... ]
        for (int i = 0; i < varList.length - 1; i+=2) {
            String var_i_stringValue = varList[i+1]; // corresponding value for variable i
            if (var_i_stringValue.isEmpty()) {
                var_i_stringValue = "0"; // default to 0
            }
//            System.out.println(varList[i] + " " + var_i_stringValue);
            vSet.set(varList[i], Double.parseDouble(var_i_stringValue));
        }
    }

    /**
     * Bound an infinite value to a finite value
     * @param value A value that may or may not be infinite
     * @param max The upper bound that this value should be shrunk to
     * @param min The lower bound that this value should be shrunk to
     * @return The new value, bounded by the maximum
     */
    public static double boundInf(double value, double min, double max) {
        if (Double.isInfinite(value)) {
            if (value < 0) {
                return min;
            } else {
                return max;
            }
        }
        return value;
    }


    public static double leftLocalTan(String func_expr, double currentX) {
        // goes l0 -> l1 -> currentX
        // find slope from l0 -> l1 to guess whether f(currentX) should be +inf or -inf
        variables.set("x", currentX - 0.01);
        double l0 = evaluator.evaluate(func_expr, variables);
        variables.set("x", currentX - 0.005);
        double l1 = evaluator.evaluate(func_expr, variables);
        return (l1 - l0) / 0.005; // return the slope
    }

    public static double rightLocalTan(String func_expr, double currentX) {
        // goes currentX -> r0 -> r1
        variables.set("x", currentX + 0.005);
        double r0 = evaluator.evaluate(func_expr, variables);
        variables.set("x", currentX + 0.01);
        double r1 = evaluator.evaluate(func_expr, variables);
        return (r1 - r0) / 0.005; // return the slope
    }
}
