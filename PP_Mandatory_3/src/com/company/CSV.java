package com.company;

import com.company.parallel.Parallel;
import com.company.sequential.Sequential;

import java.util.ArrayList;

public class CSV {
    public static void main(String[] args) {
        int nThread = Runtime.getRuntime().availableProcessors();
        int r = 100;

        System.out.println("n,sequential_sieve,sequential_factorization,parallel_sieve," +
                "parallel_factorization,sieve_speedup,factorization_speedup");

        for (long n = 2000000; n < 2000000001; n *= 10)
            for (int loop = 0; loop < 7; loop++) {
                System.out.print(n + ",");
                long startSeqSieve, finishSeqSieve, totalSeqSieve;
                int[] sequentialSieve;
                startSeqSieve = System.nanoTime();
                sequentialSieve = Sequential.sequentialSieve((int) n);
                finishSeqSieve = System.nanoTime();
                totalSeqSieve = finishSeqSieve - startSeqSieve;
                System.out.print(totalSeqSieve + ",");

                long startSeqFact, finishSeqFact, totalSeqFact;
                ArrayList<ArrayList<Long>> sequentialFactorization;
                startSeqFact = System.nanoTime();
                sequentialFactorization = Sequential.sequentialFactorizationLessThan(r, (long) n * n);
                finishSeqFact = System.nanoTime();
                totalSeqFact = finishSeqFact - startSeqFact;
                System.out.print(totalSeqFact + ",");

                long startParSieve, finishParSieve, totalParSieve;
                int[] parallelSieve;
                startParSieve = System.nanoTime();
                parallelSieve = Parallel.parallelSieve((int) n, nThread);
                finishParSieve = System.nanoTime();
                totalParSieve = finishParSieve - startParSieve;
                System.out.print(totalParSieve + ",");

                long startParFact, finishParFact, totalParFact;
                ArrayList<ArrayList<Long>> parallelFactorization;
                startParFact = System.nanoTime();
                parallelFactorization = Parallel.parallelFactorizationLessThan(r, (long) n * n, nThread);
                finishParFact = System.nanoTime();
                totalParFact = finishParFact - startParFact;
                System.out.print(totalParFact + ",");

                System.out.print((double) totalSeqSieve / totalParSieve + ",");
                System.out.println((double) totalSeqFact / totalParFact);
            }
    }
}
