public class MathMatrix {
    private int size;
    private double[][] entries;
    private double det;

    public MathMatrix(double[][] entries) {
        // check if it's a square matrix
        if (!validMatrix(entries.length, entries)) {
            System.out.println("Not a valid matrix!");
            System.out.println("Defaulted to identity matrix");
            this.size = 2;
            this.entries = new double[][]{{1, 0}, {0, 1}};
        } else {
            this.size = entries.length;
            this.entries = entries;
        }
        this.det = this.entries[1][1]*this.entries[0][0] - this.entries[0][1]*this.entries[1][0];
    }

    private boolean validMatrix(int actualSize, double[][] entries) {
        if (entries.length != actualSize) {
            return false;
        } else {
            for (int r = 0; r < entries.length; r++) {
                if (entries[r].length != actualSize) {
                    return false;
                }
            }
            return true;
        }
    }

    public int getSize() { return this.size; }

    public double[][] getArray() { return this.entries; }

    /**
     * Return determinant of this MathMatrix
     * @return Determinant
     */
    public double getDet() { return this.det; }
}
