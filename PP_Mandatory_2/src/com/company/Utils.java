package com.company;

public class Utils {
    static public double[][] transpose(double[][] matrix) {
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int x = 0; x < result.length; x++) {
            for (int y = 0; y < result[x].length; y++) {//ottimizzare
                result[x][y] = matrix[y][x];
            }
        }
        return result;
    }
}
