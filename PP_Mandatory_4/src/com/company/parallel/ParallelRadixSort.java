package com.company.parallel;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ParallelRadixSort {

    // The number of bits used to represent a single digit
    int useBits;
    int[] a, b, digitFrequencies, digitPointers;
    int[][] matrixDigitFrequencies, matrixDigitPointers;
    int totalThreads;
    CyclicBarrier innerCyclicBarrier;
    CyclicBarrier mainCyclicBarrier;

    public class ParallelCountingSort implements Runnable {
        int threadIndex;
        int[] a, b;
        int mask, shift;
        int fromIndex, toIndex;

        public ParallelCountingSort(int threadIndex, int[] a, int[] b, int mask, int shift) {
            this.threadIndex = threadIndex;
            this.a = a;
            this.b = b;
            this.mask = mask;
            this.shift = shift;
            this.fromIndex = threadIndex * (a.length / totalThreads);
            this.toIndex = (threadIndex == totalThreads - 1) ?
                    a.length : (threadIndex + 1) * (a.length / totalThreads);
        }

        @Override
        public void run() {
            int[] digitFrequencies = new int[mask + 1];
            int[] digitPointers = new int[digitFrequencies.length];
            try {
                for (int i = fromIndex; i < toIndex; i++) {
                    digitFrequencies[(a[i] >> shift) & mask]++;
                }
                matrixDigitFrequencies[threadIndex] = digitFrequencies;
                matrixDigitPointers[threadIndex] = digitPointers;
                innerCyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            try {
                if (threadIndex == totalThreads - 1) {
                    int accumulated = 0;
                    for (int i = 0; i < digitFrequencies.length; i++) {
                        for (int j = 0; j < totalThreads; j++) {
                            matrixDigitPointers[j][i] = accumulated;
                            accumulated += matrixDigitFrequencies[j][i];
                        }
                    }
                }
                innerCyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            try {
                for (int i = fromIndex; i < toIndex; i++) {
                    b[digitPointers[(a[i] >> shift) & mask]++] = a[i];
                }
                innerCyclicBarrier.await();
                mainCyclicBarrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ParallelRadixSort(int useBits, int totalThreads) {
        this.useBits = useBits;
        this.totalThreads = totalThreads;
        this.matrixDigitFrequencies = new int[this.totalThreads][];
        this.matrixDigitPointers = new int[this.totalThreads][];
        this.innerCyclicBarrier = new CyclicBarrier(this.totalThreads);
        this.mainCyclicBarrier = new CyclicBarrier(this.totalThreads + 1);
    }

    // Counting sort. A stable sorting algorithm.
    private void countingSort(int mask, int shift) {
        try {
            for (int i = 0; i < totalThreads; i++)
                new Thread(new ParallelCountingSort(i, a, b, mask, shift)).start();
            mainCyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    // Radix sort. Uses counting sort for each position.
    public int[] radixSort(int[] unsortedArray) {

        a = unsortedArray;
        b = new int[a.length];

        // STEP A : Find the maximum value.
        int max = new ParallelFindMax(a, totalThreads).find();

        // Substep: Finding number of bits that is needed to represent max value
        int numBitsMax = 1;
        while (max >= (1L << numBitsMax))
            numBitsMax++;


        // Substep: Finding the number of positions needed to represent the max value
        int numOfPositions = numBitsMax / useBits;
        if (numBitsMax % useBits != 0)
            numOfPositions++;


        // Substep: If useBits is larger than numBitsMax,
        // set useBits equal to numBitsMax to save space.
        if (numBitsMax < useBits)
            useBits = numBitsMax;


        // Substep: Creating the mask and initialising the shift variable,
        // both of whom are used to extract the digits.
        int mask = (1 << useBits) - 1;
        int shift = 0;


        // Performing the counting sort on each position
        for (int i = 0; i < numOfPositions; i++) {
            countingSort(mask, shift);
            shift += useBits;

            // Setting array a to be the array to be sorted again
            int[] temp = a;
            a = b;
            b = temp;
        }

        return a;

    }

}