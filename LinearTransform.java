public class LinearTransform {
    public static double[] applyPointTransform(double[] point, MathMatrix matrix) {
        double[] newPoint = new double[matrix.getSize()];
        double[][] matrixArray = matrix.getArray();
        newPoint[0] = point[0]*matrixArray[0][0] + point[1]*matrixArray[1][0];
        newPoint[1] = point[0]*matrixArray[0][1] + point[1]*matrixArray[1][1];
        return newPoint;
    }

    /**
     * Returns a transformation matrix based on given string description
     * @param transform String that describes the transformation (rotate 20 deg, reflect y=7x, stretch x by 4, etc.)
     * @return Corresponding MathMatrix that performs the transformation
     */
    public static MathMatrix transform(String transform) {
        // Assume that the user will not input an invalid string
        transform = transform.toLowerCase();
        String[] input = transform.split(" ");
        switch (input[0]) { // type of transformation (e.g. "reflect", "rotate", etc.)
            case "rotate":
                if (input.length == 3 && input[2].equals("deg")) { // degrees
                    double rotateRad = Double.parseDouble(input[1]) * Math.PI / 180; // convert to radians
                    return new MathMatrix(new double[][]{
                            {Math.cos(rotateRad), -Math.sin(rotateRad)},
                            {Math.sin(rotateRad), Math.cos(rotateRad)}
                    });
                }
        }
        return null;
    }
}
