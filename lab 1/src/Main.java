public class Main {
    static void printRes(double[][] z1) {
        for (int line = 0; line < z1.length; line++) {
            for (int row = 0; row < z1[line].length; row++) {
                System.out.printf("%9.4f", z1[line][row]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        double[][] z1 = new double[7][12];
        long[] r = new long[7];
        int[] z = new int[7];
        float[] x = new float[12];
        int p = 0;
        // 1
        for (long i = 17; i >= 5; i--) {
            if (i % 2 != 0) {
                r[p] = i;
                p++;
            }
        }
        // 2
        for (int i = 0; i < x.length; i++) {
            x[i] = -12.0f + (float) Math.random() * (8.0f + 12.0f);
        }
        // 3
        for (int i = 0; i < z1.length; i++) {
            for (int j = 0; j < z1[i].length; j++) {
                z1[i][j] = count(z[i], x[j]);
            }
        }
        printRes(z1);
    }
    public static double count(int z, float x) {
        double ret;
        switch (z) {
            case 7:
                ret = Math.log10(Math.pow(Math.E, Math.pow(x - 2, ((Math.pow(Math.E, x)) / (1 - Math.pow(x, (x + Math.PI) / x))))));
                break;
            case 5,9,17:
                ret = Math.atan(Math.sin(Math.log10(Math.acos(((x - 2) / 2) * Math.E))));
                break;
            default:
                ret = Math.cbrt(Math.pow((Math.pow((Math.PI * Math.pow(0.75 / (x + 1), x)), 2)), 2));
                break;
        }
        return ret;
    }
}

//java -jar Lab.jar
//jar -c -f Lab.jar -e Main Main.class