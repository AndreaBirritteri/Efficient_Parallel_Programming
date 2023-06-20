package com.company;

import com.company.parallel.ParallelRadixSort;
import com.company.precode.Oblig4Precode;
import com.company.precode.RadixSort;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int totalThreads;
        int n, seed, useBits;

        try {
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
            useBits = Integer.parseInt(args[2]);
            totalThreads = args.length == 3 ?
                    Runtime.getRuntime().availableProcessors() : Integer.parseInt(args[3]);
        } catch (Exception e) {
            System.out.println("Correct usage is: <n> <seed> <useBits> [totalThreads]");
            return;

        }
        System.out.println("You are using " + totalThreads + " threads.\n");
        // Quick check to see if sorted (takes a few seconds at high n's)
        int[] arraySort = Oblig4Precode.generateArray(n, seed);
        long startTimeJava = System.nanoTime();
        Arrays.sort(arraySort);
        long finishTimeJava = System.nanoTime();
        long totalTimeJava = finishTimeJava - startTimeJava;
        System.out.println(totalTimeJava + " <- Java sort time");

        // Radix sorting sequential
        int[] as = Oblig4Precode.generateArray(n, seed);
        RadixSort rss = new RadixSort(useBits);
        long startTimeSequential = System.nanoTime();
        as = rss.radixSort(as);
        long finishTimeSequential = System.nanoTime();
        long totalTimeSequential = finishTimeSequential - startTimeSequential;
        System.out.println(totalTimeSequential + " <- Sequential radix sort time");
        System.out.println("Arrays are equal: " + Arrays.equals(arraySort, as));
        Oblig4Precode.saveResults(Oblig4Precode.Algorithm.SEQ, seed, as);

        // Radix sorting parallel
        int[] ap = Oblig4Precode.generateArray(n, seed);
        ParallelRadixSort rs = new ParallelRadixSort(useBits,totalThreads);
        long startTimeParallel = System.nanoTime();
        ap = rs.radixSort(ap);
        long finishTimeParallel = System.nanoTime();
        long totalTimeParallel = finishTimeParallel - startTimeParallel;
        System.out.println(totalTimeParallel + " <- Parallel radix sort time");
        System.out.println("Arrays are equal: " + Arrays.equals(arraySort, ap));
        Oblig4Precode.saveResults(Oblig4Precode.Algorithm.PAR, seed, ap);
    }
}
