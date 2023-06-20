package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * IN3030 - Oblig 2 Pre-code usage example
 *
 * @author Magnus Espeland <magnuesp@ifi.uio.no>
 * @date 2019.02.14
 */

public class Main {

    public static double matrixMul(double[][] a, double[][] b, double[][] c, int n, int randomSeed, Oblig2Precode.Mode mode, int threadNumber) {
        double startTime = 0, finishTime = -1000000.0;

        switch (mode) {
            case PARA_NOT_TRANSPOSED -> {
                startTime = System.nanoTime();
                Parallel.notTransposed(a, b, c, n, threadNumber);
                finishTime = System.nanoTime();
            }
            case PARA_B_TRANSPOSED -> {
                startTime = System.nanoTime();
                Parallel.bTransposed(a, b, c, n, threadNumber);
                finishTime = System.nanoTime();
            }
            case PARA_A_TRANSPOSED -> {
                startTime = System.nanoTime();
                Parallel.aTransposed(a, b, c, n, threadNumber);
                finishTime = System.nanoTime();
            }
        }
        Oblig2Precode.saveResult(randomSeed, mode, c);
        return (finishTime - startTime) / 1000000.0;
    }

    public static double matrixMul(double[][] a, double[][] b, double[][] c, int n, int randomSeed, Oblig2Precode.Mode mode) {
        double startTime = 0, finishTime = -1000000.0;

        switch (mode) {
            case SEQ_NOT_TRANSPOSED -> {
                startTime = System.nanoTime();
                Sequential.notTransposed(a, b, c, n);
                finishTime = System.nanoTime();
            }
            case SEQ_B_TRANSPOSED -> {
                startTime = System.nanoTime();
                Sequential.bTransposed(a, b, c, n);
                finishTime = System.nanoTime();
            }
            case SEQ_A_TRANSPOSED -> {
                startTime = System.nanoTime();
                Sequential.aTransposed(a, b, c, n);
                finishTime = System.nanoTime();
            }
        }
        Oblig2Precode.saveResult(randomSeed, mode, c);
        return (finishTime - startTime) / 1000000.0;
    }

    public static void main(String[] args) {

        // Process these from the command line
        //100 200 500 1000
        int threadNumber;
        int n;
        int seed;

        if (args.length == 1 && Objects.equals(args[0], "-h")) {
            System.out.println("use [threadNumber] [size of the squared matrix] [seed random number]");
        } else if (args.length != 3 && args.length != 2) {
            System.out.println("Wrong or no arguments. Use -h to get help");
        } else {
            threadNumber = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);

            if (args.length == 2) {
                seed = new Random().nextInt(0, 9999999);
                System.out.println("You didn't input a seed. Using '" + seed + "' as seed");
            }else{
                seed = Integer.parseInt(args[2]);
            }
            // Get the matrices
            double[][] a = Oblig2Precode.generateMatrixA(seed, n);
            double[][] b = Oblig2Precode.generateMatrixB(seed, n);

            // The result matrix
            double[][] matSeqNotT = new double[n][n], matSeqBT = new double[n][n], matSeqAT = new double[n][n];
            double[][] matParNotT = new double[n][n], matParBT = new double[n][n], matParAT = new double[n][n];
            double seqNotTTime, seqATTime, seqBTTime, parNotTTime, parATTime, parBTTime;

            seqNotTTime = matrixMul(a, b, matSeqNotT, n, seed, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED);
            System.out.println(seqNotTTime + " - SEQ_NOT_TRANSPOSED");
            seqBTTime = matrixMul(a, b, matSeqBT, n, seed, Oblig2Precode.Mode.SEQ_B_TRANSPOSED);
            System.out.println(seqBTTime + " - SEQ_B_TRANSPOSED");
            seqATTime = matrixMul(a, b, matSeqAT, n, seed, Oblig2Precode.Mode.SEQ_A_TRANSPOSED);
            System.out.println(seqATTime + " - SEQ_A_TRANSPOSED");
            parNotTTime = matrixMul(a, b, matParNotT, n, seed, Oblig2Precode.Mode.PARA_NOT_TRANSPOSED, threadNumber);
            System.out.println(parNotTTime + " - PARA_NOT_TRANSPOSED");
            parBTTime = matrixMul(a, b, matParBT, n, seed, Oblig2Precode.Mode.PARA_B_TRANSPOSED, threadNumber);
            System.out.println(parBTTime + " - PARA_B_TRANSPOSED");
            parATTime = matrixMul(a, b, matParAT, n, seed, Oblig2Precode.Mode.PARA_A_TRANSPOSED, threadNumber);
            System.out.println(parATTime + " - PARA_A_TRANSPOSED");

            boolean correct = true;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    correct = correct && matSeqBT[i][j] == matSeqNotT[i][j];
                    correct = correct && matSeqAT[i][j] == matSeqNotT[i][j];
                    correct = correct && matParNotT[i][j] == matSeqNotT[i][j];
                    correct = correct && matParBT[i][j] == matSeqNotT[i][j];
                    correct = correct && matParAT[i][j] == matSeqNotT[i][j];
                }
            }
            if(correct)
                System.out.println("The result is correct.");
            else
                System.out.println("Something went wrong!");
        }
    }
}

