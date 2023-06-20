package com.company.sequential;

import com.company.precode.SieveOfEratosthenes;

import java.util.ArrayList;

public class Sequential {

    public static int[] sequentialSieve(int n) {
        return new SieveOfEratosthenes(n).getPrimes();
    }

    public static ArrayList<ArrayList<Long>> sequentialFactorizationLessThan(int r, long n) {
        ArrayList<ArrayList<Long>> result = new ArrayList<>();
        int[] primes = sequentialSieve((int) Math.sqrt(n + 1));

        for (int i = 0; i < r; i++) {
            ArrayList<Long> temp = makeSequentialFactorization(n - i, primes);
            result.add(temp);
        }

        return result;
    }

    public static ArrayList<Long> sequentialFactorization(long n) {
        int[] primesToNumber = new SieveOfEratosthenes((int) Math.sqrt(n + 1)).getPrimes();
        return makeSequentialFactorization(n, primesToNumber);
    }

    public static ArrayList<Long> makeSequentialFactorization(long n, int[] primesToNumber) {
        ArrayList<Long> listOfPrimes = new ArrayList<>();

        long thisNumber = n;
        for (long primeNumber : primesToNumber) {
            if (n % primeNumber == 0) {
                while (thisNumber > 1 && thisNumber % primeNumber == 0) {
                    listOfPrimes.add(primeNumber);
                    thisNumber /= primeNumber;
                }
            }
        }
        if(thisNumber != 1)
            listOfPrimes.add(thisNumber);

        return listOfPrimes;
    }
}
