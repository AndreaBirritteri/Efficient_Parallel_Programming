package com.company.parallel;

import java.util.concurrent.*;

public class ParallelFindMax {
    int[] numbers, localMaxesArray;
    CyclicBarrier cyclicBarrier;
    int nThreads, nPerThread;

    public ParallelFindMax(int[] numbers, int nThreads) {
        this.numbers = numbers;
        this.nThreads = nThreads;
        this.localMaxesArray = new int[nThreads];
    }

    public int find() {
        int totalMax = -1;
        nPerThread = (int) Math.ceil((float) numbers.length / nThreads);
        cyclicBarrier = new CyclicBarrier(nThreads + 1);

        try {
            for (int i = 0; i < nThreads; i++)
                new Thread(new FindMaxWorker(i)).start();
            cyclicBarrier.await();

            for (int i = 0; i < nThreads; i++)
                if (localMaxesArray[i] > totalMax)
                    totalMax = localMaxesArray[i];

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalMax;
    }

    class FindMaxWorker implements Runnable {
        int threadIndex, localMax = -1;

        FindMaxWorker(int threadIndex) {
            this.threadIndex = threadIndex;
        }

        public void run() {
            int fromIndex = threadIndex * nPerThread;
            int toIndex = threadIndex * nPerThread + nPerThread;

            if (fromIndex >= numbers.length)
                fromIndex = numbers.length - 1;
            if (toIndex >= numbers.length)
                toIndex = numbers.length - 1;

            try {
                for (int i = fromIndex; i < toIndex; i++) {
                    if (numbers[i] > localMax)
                        localMax = numbers[i];
                }
                localMaxesArray[threadIndex] = localMax;

                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}