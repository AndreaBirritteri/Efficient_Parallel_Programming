package com.company;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Parallel {
    static int[] array;
    static int[][] Ks;


    static class Worker implements Runnable {
        int start, end, k, threadCount;
        CyclicBarrier barrier;


        Worker(CyclicBarrier barrier, int start, int end, int k, int threadCount) {
            this.threadCount = threadCount;
            this.start = start;
            this.end = end;
            this.k = k;
            this.barrier = barrier;
        }

        @Override
        public void run() {

            try {

                for(int j = 0; j < k; j++)
                    Ks[threadCount][j] = array[start + j];

                Sequential.sequentialWebSearchSort(Ks[threadCount], k);

                for (int j = start + k; j < end; j++) {
                    //int supp = array[k - 1];
                    if (array[j] > Ks[threadCount][k - 1]) {
                        Ks[threadCount][k - 1] = array[j];
                        int switchCount = k - 2;
                        while (switchCount > 0 && Ks[threadCount][switchCount] < Ks[threadCount][switchCount + 1]) {
                            int supp = Ks[threadCount][switchCount];
                            Ks[threadCount][switchCount] = Ks[threadCount][switchCount + 1];
                            Ks[threadCount][switchCount + 1] = supp;
                            switchCount--;
                        }

                        //array[j] = supp;
                        //insertSortDesc(Ks[threadCount], 0, k - 1); //farlo al contrario
                        //a dir la verità non interessa molto ordinarlo ma vedremo
                    }
                }
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    static public void parallelWebSearchSort(int threadNumber, int[] a, int[] result, int k) {
        array = a;
        Ks = new int[threadNumber][k];
        int elePerThread = array.length / threadNumber;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber + 1);

        Worker[] workers = new Worker[threadNumber];
        Thread[] threads = new Thread[threadNumber];

        for (int i = 0; i < threadNumber; i++) {
            int startAt = (i * elePerThread);
            int endAt = (startAt + elePerThread);
            //if (endAt >= array.length)
            //endAt = array.length - 1;
            workers[i] = new Worker(cyclicBarrier, startAt, endAt, k, i);
            threads[i] = new Thread(workers[i]);
            threads[i].start();
        }

        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < threadNumber; i++)
            for (int j = 0; j < Ks[i].length; j++) {
                int thisK = Ks[i][j];
                if (thisK > result[k - 1]) {
                    result[k - 1] = thisK;
                    int switchCount = k - 2;
                    while (switchCount >= 0 && result[switchCount] < result[switchCount + 1]) {
                        int supp = result[switchCount];
                        result[switchCount] = result[switchCount + 1];
                        result[switchCount + 1] = supp;
                        switchCount--;
                    }
                }

            }

        /*
        int pippo = 0;
        for (int i = 0; i < threadNumber; i++)
            for (int j = 0; k < Ks[i].length; j++) {
                a[pippo] = Ks[i][j];
            }

        Sequential.sequentialWebSearchSort(a, k);

*/
    }
}