package com.company;

import com.company.parallel.ParallelRadixSort;
import com.company.precode.Oblig4Precode;
import com.company.precode.RadixSort;

import java.util.Arrays;

public class CSV {
    public static void main(String[] args) {
        int totalThreads = Runtime.getRuntime().availableProcessors();
        int seed, useBits;

        try {
            seed = Integer.parseInt(args[0]);
            useBits = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Correct usage for CSV is: <seed> <useBits>");
            return;

        }

        System.out.println("n,java,sequential,parallel");
        for (int n = 1000; n <= 100000000; n *= 10)
            for (int loop = 0; loop < 7; loop++) {
                System.out.print(n + ",");
                int[] arraySort = Oblig4Precode.generateArray(n, seed);
                long startTimeJava = System.nanoTime();
                Arrays.sort(arraySort);
                long finishTimeJava = System.nanoTime();
                long totalTimeJava = finishTimeJava - startTimeJava;
                System.out.print(totalTimeJava + ",");

                // Radix sorting sequential
                int[] as = Oblig4Precode.generateArray(n, seed);
                RadixSort rss = new RadixSort(useBits);
                long startTimeSequential = System.nanoTime();
                as = rss.radixSort(as);
                long finishTimeSequential = System.nanoTime();
                long totalTimeSequential = finishTimeSequential - startTimeSequential;
                System.out.print(totalTimeSequential + ",");

                // Radix sorting parallel
                int[] ap = Oblig4Precode.generateArray(n, seed);
                ParallelRadixSort rs = new ParallelRadixSort(useBits, totalThreads);
                long startTimeParallel = System.nanoTime();
                ap = rs.radixSort(ap);
                long finishTimeParallel = System.nanoTime();
                long totalTimeParallel = finishTimeParallel - startTimeParallel;
                System.out.println(totalTimeParallel);
            }
    }
}
