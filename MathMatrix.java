public class MathMatrix {
    private int size;
    private double[][] entries;

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
}
