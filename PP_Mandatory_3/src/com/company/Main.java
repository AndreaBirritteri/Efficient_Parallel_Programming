package com.company;

import com.company.parallel.Parallel;
import com.company.precode.Oblig3Precode;
import com.company.sequential.Sequential;

import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {


        if (args.length == 1 && Objects.equals(args[0], "-h")) {
            System.out.println("Use [number] [threadNumber]");
        } else if (args.length != 2) {
            System.out.println("Wrong or no arguments. Use -h to get help");
        } else {
            int n = Integer.parseInt(args[0]);
            if(n <= 16) {
                System.out.println("[n] must be greater then 16");
                return;
            }

            int nThread = Integer.parseInt(args[1]) <= 0 ?
                    Runtime.getRuntime().availableProcessors() : Integer.parseInt(args[1]);
            int r = 100;

            System.out.println("Using " + nThread + " threads\nStarted...\n");

            long startSeqSieve, finishSeqSieve, totalSeqSieve;
            int[] sequentialSieve;
            System.out.println("Calculating all the primes to " + n + " sequentially...");
            startSeqSieve = System.nanoTime();
            sequentialSieve = Sequential.sequentialSieve(n);
            finishSeqSieve = System.nanoTime();
            totalSeqSieve = finishSeqSieve - startSeqSieve;
            System.out.println(totalSeqSieve + "ns <- Sequential sieve");
            System.out.println("Length -> " + sequentialSieve.length + "\n");

            long startSeqFact, finishSeqFact, totalSeqFact;
            ArrayList<ArrayList<Long>> sequentialFactorization;
            System.out.println("Calculating the prime factors for the " + r
                    + " numbers less than " + (long)n * n + " sequentially...");
            startSeqFact = System.nanoTime();
            sequentialFactorization = Sequential.sequentialFactorizationLessThan(r, (long) n * n);
            finishSeqFact = System.nanoTime();
            totalSeqFact = finishSeqFact - startSeqFact;

            Oblig3Precode sequentialPrecode = new Oblig3Precode(n);
            for (int i = 0; i < sequentialFactorization.size(); i++) {
                for (int j = 0; j < sequentialFactorization.get(i).size(); j++) {
                    sequentialPrecode.addFactor(((long) n * n) - i, sequentialFactorization.get(i).get(j));
                }
            }
            sequentialPrecode.writeFactors("sequential");

            System.out.println("Array -> " + sequentialFactorization);
            System.out.println(totalSeqFact + "ns <- Sequential factorization\n\n");

            long startParSieve, finishParSieve, totalParSieve;
            int[] parallelSieve;
            System.out.println("Calculating all the primes to " + n + " in parallel...");
            startParSieve = System.nanoTime();
            parallelSieve = Parallel.parallelSieve(n, nThread);
            finishParSieve = System.nanoTime();
            totalParSieve = finishParSieve - startParSieve;
            System.out.println(totalParSieve + "ns <- Parallel sieve");
            System.out.println("Length -> " + parallelSieve.length + "\n");

            long startParFact, finishParFact, totalParFact;
            ArrayList<ArrayList<Long>> parallelFactorization;
            System.out.println("Calculating the prime factors for the " + r
                    + " numbers less than " + (long)n * n + " in parallel...");
            startParFact = System.nanoTime();
            parallelFactorization = Parallel.parallelFactorizationLessThan(r, (long) n * n, nThread);
            finishParFact = System.nanoTime();
            totalParFact = finishParFact - startParFact;
            System.out.println("Array -> " + parallelFactorization);
            System.out.println(totalParFact + "ns <- Parallel factorization\n\n");

            Oblig3Precode parallelPrecode = new Oblig3Precode(n);
            for (int i = 0; i < parallelFactorization.size(); i++) {
                for (int j = 0; j < parallelFactorization.get(i).size(); j++) {
                    parallelPrecode.addFactor(((long) n * n) - i, parallelFactorization.get(i).get(j));
                }
            }
            parallelPrecode.writeFactors("parallel");

            System.out.println("SpeedUp sieve -> " + (double) totalSeqSieve / totalParSieve);
            System.out.println("Speedup factorization -> " + (double) totalSeqFact / totalParFact);
        }
    }
}
