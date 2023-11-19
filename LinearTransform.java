public class LinearTransform {
    public static double[] applyPointTransform(double[] point, MathMatrix matrix) {
        double[] newPoint = new double[matrix.getSize()];
        double[][] matrixArray = matrix.getArray();
        newPoint[0] = point[0]*matrixArray[0][0] + point[1]*matrixArray[1][0];
        newPoint[1] = point[0]*matrixArray[0][1] + point[1]*matrixArray[1][1];
        return newPoint;
    }
}
