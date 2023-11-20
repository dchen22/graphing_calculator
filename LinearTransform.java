import com.fathzer.soft.javaluator.DoubleEvaluator;
import TRANSFORMATIONS.*;

import java.util.HashMap;

public class LinearTransform {
    private static DoubleEvaluator evaluator = new DoubleEvaluator(); // evaluator for this expression

    public static double[] applyPointTransform(double[] point, MathMatrix matrix) {
        double[] newPoint = new double[matrix.getSize()];
        double[][] matrixArray = matrix.getArray();
        newPoint[0] = matrixArray[0][0]*point[0] + matrixArray[0][1]*point[1];
        newPoint[1] = matrixArray[1][0]*point[0] + matrixArray[1][1]*point[1];
        return newPoint;
    }

    /**
     * Returns a transformation matrix based on given string description.
     * The following commands are available (#: number, W: variable):
     *
     * rotate # deg, rotate #, reflect y=#x, stretch W #, stretch W1 #1 W2 #2,
     * project y=#x
     * @param transform String that describes the transformation (rotate 20 deg, reflect y=7x, stretch x by 4, etc.)
     * @return Corresponding MathMatrix that performs the transformation
     */
    public static MathMatrix transform(String transform) {
        // Assume that the user will not input an invalid string
        transform = transform.toLowerCase();
        String[] input = transform.split(" ");
        switch (input[0]) { // type of transformation (e.g. "reflect", "rotate", etc.)
            case "rotate":
                if (input.length >= 2) { // just making sure
                    double rotateAmount = evaluator.evaluate(input[1]); // raw value
                    if (input.length >= 3) { // if there are at least 3 arguments in the command string
                        if (input[2].equals("deg")) { // user inputted value in degrees
                            rotateAmount *= Math.PI / 180; // convert to radians
                        }
                    }
                    return new MathMatrix(new double[][]{
                            {Math.cos(rotateAmount), -Math.sin(rotateAmount)},
                            {Math.sin(rotateAmount), Math.cos(rotateAmount)}
                    });
                }
                break;
            case "reflect":
                // input[1] must be in the form y=mx for some real number m
                // take substring from after character y to before x
                String slope = input[1].substring(2, input[1].length() - 1);
                System.out.println(slope);
                double m = 1; // default value; y=x with no explicit coefficient
                if (!(slope.isEmpty())) { // y=mx, no coefficient that must be evaluated
                    m = evaluator.evaluate(slope);
                }

                double m2 = Math.pow(m, 2); // slope squared (m^2)
                double c = 1 / (m2 + 1); // coefficient 1 / (m^2 + 1)
                return new MathMatrix(new double[][]{
                        {c*(1 - m2), c*2*m},
                        {c*2*m, c*(m2 - 1)}
                });
            case "stretch":
                HashMap<String, Double> stretchAmount = new HashMap<>();
                stretchAmount.put("x", 1.0); // default value
                stretchAmount.put("y", 1.0); // default value

                stretchAmount.put(input[1], evaluator.evaluate(input[2])); // stretch W by #

                // if there are 2 stretching coordinates
                if (input.length == 7) { // stretch W1 #1 W2 #2
                    stretchAmount.put(input[3], evaluator.evaluate(input[4]));
                }


                return new MathMatrix(new double[][]{
                        {stretchAmount.get("x"), 0},
                        {0, stretchAmount.get("y")}
                });
            case "project":
                slope = input[1].substring(2, input[1].length() - 1);
                System.out.println(slope);
                m = 1; // default value; y=x with no explicit coefficient
                if (!(slope.isEmpty())) { // y=mx, no coefficient that must be evaluated
                    m = evaluator.evaluate(slope);
                }

                m2 = Math.pow(m, 2); // slope squared (m^2)
                c = 1 / (m2 + 1); // coefficient 1 / (m^2 + 1)
                return new MathMatrix(new double[][]{
                        {c, c*m},
                        {c*m, c*m2}
                });
        }
        return null;
    }

}
