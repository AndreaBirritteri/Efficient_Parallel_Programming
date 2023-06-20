package com.company.parallel;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SieveOfEratosthenesParallel {
    int n, root, numOfPrimes, nThreads;
    byte[] oddNumbers;
    static CyclicBarrier cyclicBarrier;

    public class FirstSieveWorker implements Runnable {
        int finishNum;

        FirstSieveWorker(int toByte) {
            this.finishNum = toByte * 16 + 16;
        }

        @Override
        public void run() {
            try {
                int step;
                for (int prime = 3; prime <= root; prime += 2) {
                    if (isPrime(prime)) {
                        step = prime;
                        while (prime * step <= finishNum) {
                            mark(prime * step);
                            step += 2;
                        }
                    }
                }
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public class SieveWorker implements Runnable {
        private final int startNum;
        private final int finishNum;

        public SieveWorker(int fromByte, int toByte) {
            this.startNum = fromByte * 16 + 1;
            this.finishNum = toByte * 16 + 16;
        }

        @Override
        public void run() {
            try {
                int prime = nextPrime(1);
                while (prime <= root) {
                    int step = (int) Math.ceil((double) startNum / prime);
                    if (step % 2 == 0)
                        step++;
                    while (prime * step <= finishNum) {
                        try {
                            mark(prime * step);
                        } catch (Exception e) {
                            return;
                        }
                        step += 2;
                    }
                    prime = nextPrime(prime);

                    if (prime == -1)
                        break;
                }
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public SieveOfEratosthenesParallel(int n, int nThreads) {
        this.n = n;
        root = (int) Math.sqrt(n);
        oddNumbers = new byte[(n / 16) + 1];
        this.nThreads = nThreads;
    }

    public int[] getPrimes() {
        if (n <= 1)
            return new int[0];

        sieve();

        return collectPrimes();
    }

    private int[] collectPrimes() {

        for (int i = 3; i <= n; i += 2)
            if (isPrime(i))
                numOfPrimes++;

        int[] primes = new int[numOfPrimes];

        primes[0] = 2;

        int j = 1;
        for (int i = 3; i <= n; i += 2)
            if (isPrime(i))
                primes[j++] = i;

        return primes;
    }

    private void sieve() {
        try {
            mark(1);
            numOfPrimes = 1;

            cyclicBarrier = new CyclicBarrier(nThreads + 1);

            int sqrtByte = root / 16;
            int startByte = sqrtByte + 1;
            int bytesPerThread = oddNumbers.length - sqrtByte - 1;


            new Thread(new FirstSieveWorker((bytesPerThread / nThreads) + startByte - 1)).start();

            for (int t = 1; t < nThreads; t++) {
                int fromByte = (t * bytesPerThread / nThreads) + startByte;
                int toByte = ((t + 1) * bytesPerThread / nThreads) + startByte - 1;
                if (t == nThreads - 1)
                    toByte = oddNumbers.length - 1;

                new Thread(new SieveWorker(fromByte, toByte)).start();
            }
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private int nextPrime(int prev) {
        for (int i = prev + 2; i <= root; i += 2)
            if (isPrime(i))
                return i;

        return -1;
    }

    private boolean isPrime(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }

    private void mark(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;
        oddNumbers[byteIndex] |= (1 << bitIndex);
    }

    static void printPrimes(int[] primes) {
        for (int prime : primes)
            System.out.println(prime);
    }
}