package com.company;

public class Sequential {
    static void notTransposed(double[][] a, double[][] b, double[][] c,  int n) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++)
                    c[i][j] += a[i][k] * b[k][j];
    }

    static void aTransposed(double[][] a, double[][] b, double[][] c, int n) {
        double[][] aT = Utils.transpose(a);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++)
                    c[i][j] += aT[k][i] * b[k][j];
    }

    static void bTransposed(double[][] a, double[][] b,double[][] c,  int n) {
        double[][] bT = Utils.transpose(b);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++)
                    c[i][j] += a[i][k] * bT[j][k];
    }
}
