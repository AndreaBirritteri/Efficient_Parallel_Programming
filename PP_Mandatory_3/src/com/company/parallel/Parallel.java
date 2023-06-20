package com.company.parallel;

import com.company.sequential.Sequential;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Parallel {
    public static int[] parallelSieve(int n, int nThreads) {
        return new SieveOfEratosthenesParallel(n, nThreads).getPrimes();
    }

    public static ArrayList<Long> parallelFactorization(long n, int[] primesToNumber) {
        return Sequential.makeSequentialFactorization(n, primesToNumber);
    }

    public static ArrayList<ArrayList<Long>> parallelFactorizationLessThan(int r, long n, int nThreads) {
        ArrayList<ArrayList<Long>> result = new ArrayList<>();
        int[] primes = parallelSieve((int) Math.sqrt(n), nThreads);

        for (int round = 0; round < r; round++) {
            long number = n - round;
            WorkerFactorize[] workers = new WorkerFactorize[nThreads];
            CyclicBarrier cyclicBarrier = new CyclicBarrier(nThreads + 1);
            for (int i = 0; i < nThreads; i++) {
                int start = i * (primes.length / nThreads);
                int finish = start + (primes.length / nThreads) - 1;
                if (i == nThreads - 1)
                    finish = primes.length - 1;

                workers[i] = new WorkerFactorize(number, primes, start, finish, cyclicBarrier);
                new Thread(workers[i]).start();
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            result.add(new ArrayList<>());
            for (int i = 0; i < nThreads; i++) {
                result.get(result.size() - 1).addAll(workers[i].getListOfFactors());
            }
            long prod = 1;
            for (long mul : result.get(result.size() - 1)) {
                prod *= mul;
            }

            if (number / prod > 1)
                result.get(result.size() - 1).add(number / prod);

            result.get(result.size() - 1).sort(Comparator.naturalOrder());
        }

        return result;
    }


    private static class WorkerFactorize implements Runnable {
        private final long n;
        private final int[] primes;
        private final int start, finish;
        private final ArrayList<Long> listOfFactors;
        private final CyclicBarrier cyclicBarrier;

        public WorkerFactorize(long n, int[] primes, int start, int finish, CyclicBarrier cyclicBarrier) {
            this.n = n;
            this.primes = primes;
            this.start = start;
            this.finish = finish;
            this.listOfFactors = new ArrayList<>();
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            long thisNumber = n;
            for (int i = start; i <= finish; i++) {
                long primeNumber = primes[i];
                if (n % primeNumber == 0) {
                    while (thisNumber > 1 && thisNumber % primeNumber == 0) {
                        listOfFactors.add(primeNumber);
                        thisNumber /= primeNumber;
                        if (thisNumber == 1657) {
                            System.out.println(listOfFactors);
                            System.out.println(Thread.currentThread().getId() + " " + thisNumber);
                        }
                    }
                }
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        public ArrayList<Long> getListOfFactors() {
            return listOfFactors;
        }
    }
}
