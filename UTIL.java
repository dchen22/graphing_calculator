import com.fathzer.soft.javaluator.StaticVariableSet;

public class UTIL {
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
}
