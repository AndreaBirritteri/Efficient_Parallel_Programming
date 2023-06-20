package com.company;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Parallel {
    private static class Worker implements Runnable {
        double[][] a, b, c;
        int n, to, from;
        CyclicBarrier cyclicBarrier;
        Oblig2Precode.Mode mode;

        Worker(CyclicBarrier cyclicBarrier, double[][] a, double[][] b, double[][] c, int n, int from, int to, Oblig2Precode.Mode mode) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.n = n;
            this.from = from;
            this.to = to;
            this.cyclicBarrier = cyclicBarrier;
            this.mode = mode;
        }

        @Override
        public void run() {
            switch (mode) {
                case PARA_NOT_TRANSPOSED -> {
                    for (int i = from; i < to; i++)
                        for (int j = 0; j < n; j++)
                            for (int k = 0; k < n; k++)
                                c[i][j] += a[i][k] * b[k][j];
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
                case PARA_A_TRANSPOSED -> {
                    for (int i = from; i < to; i++)
                        for (int j = 0; j < n; j++)
                            for (int k = 0; k < n; k++)
                                c[i][j] += a[k][i] * b[k][j];
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
                case PARA_B_TRANSPOSED -> {
                    for (int i = from; i < to; i++)
                        for (int j = 0; j < n; j++)
                            for (int k = 0; k < n; k++)
                                c[i][j] += a[i][k] * b[j][k];
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    private static void parallelMul(double[][] a, double[][] b, double[][] c, int n, int threadNumber, Oblig2Precode.Mode mode) {
        int elePerThread = (int) Math.ceil((float) n / threadNumber);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber + 1);

        Worker[] workers = new Worker[threadNumber];
        Thread[] threads = new Thread[threadNumber];

        int t = 0;
        int startAt = 0;
        int finishAt = elePerThread;

        while (t < threadNumber) {
            workers[t] = new Worker(cyclicBarrier, a, b, c, n, startAt, finishAt, mode);
            startAt += elePerThread;
            finishAt += elePerThread;
            if (finishAt > n)
                finishAt = n;
            t++;
        }

        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new Thread(workers[i]);
            threads[i].start();
        }

        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    static void notTransposed(double[][] a, double[][] b, double[][] c, int n, int threadNumber) {
        parallelMul(a, b, c, n, threadNumber, Oblig2Precode.Mode.PARA_NOT_TRANSPOSED);
    }

    static void aTransposed(double[][] a, double[][] b, double[][] c, int n, int threadNumber) {
        a = Utils.transpose(a);
        parallelMul(a, b, c, n, threadNumber, Oblig2Precode.Mode.PARA_A_TRANSPOSED);
    }

    static void bTransposed(double[][] a, double[][] b, double[][] c, int n, int threadNumber) {
        b = Utils.transpose(b);
        parallelMul(a, b, c, n, threadNumber, Oblig2Precode.Mode.PARA_B_TRANSPOSED);
    }
}
