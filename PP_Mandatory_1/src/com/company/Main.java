package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static com.company.Parallel.parallelWebSearchSort;
import static com.company.Sequential.sequentialWebSearchSort;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        //int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);

        for (int n = 1000; n <= 100000000; n *= 10) {
            for (int j = 0; j < 7; j++) {
                assert k > 0 && n >= k;

                int[] parallelInitial = new int[n];
                int[] parallelSort = new int[k];
                int[] sequentialSort = new int[n];
                Integer[] javaSort = new Integer[n];
                for (int i = 0; i < n; i++) {
                    int q = r.nextInt(n);
                    parallelInitial[i] = q;
                    sequentialSort[i] = q;
                    javaSort[i] = q;
                }

                long startTime, finishTime;
                double javaTime, seqTime, parTime;

                System.out.println("Java sort begins");
                startTime = System.nanoTime();
                Arrays.sort(javaSort, Collections.reverseOrder());
                finishTime = System.nanoTime();
                javaTime = (finishTime - startTime) / 1000000.0;
                System.out.println(javaTime + "ms in classic sort");

                System.out.println("Sequential sort begins");
                startTime = System.nanoTime();
                sequentialWebSearchSort(sequentialSort, k);
                finishTime = System.nanoTime();
                seqTime = (finishTime - startTime) / 1000000.0;
                System.out.println(seqTime + "ms in sequential sort");

                System.out.println("Parallel sort begins");
                startTime = System.nanoTime();
                parallelWebSearchSort(8, parallelInitial, parallelSort, k);
                finishTime = System.nanoTime();
                parTime = ((finishTime - startTime) / 1000000.0);
                System.out.println(parTime + "ms in my sort");

                System.out.println("Checking if is correct.");
                boolean isCorrect = true;
                for (int i = 0; i < k; i++) {
                    //if (parallelSort[i] != sequentialSort[i]) {
                    if (parallelSort[i] != javaSort[i] || sequentialSort[i] != javaSort[i]) {
                        isCorrect = false;
                        break;
                    }
                }

                if (isCorrect)
                    System.out.println("The array is ordered correctly.");
                else
                    System.out.println("Something went wrong during ordering.");

                try {
                    File myFile = new File("stats.csv");
                    if (myFile.createNewFile()) {
                        System.out.println("File created: " + myFile.getName());
                        FileWriter myWriter = new FileWriter("stats.csv");
                        myWriter.append("n;k;java;sequential;parallel\n");
                        myWriter.close();
                    } else {
                        System.out.println("Adding line to the file.");
                    }
                    FileWriter myWriter = new FileWriter("stats.csv", true);
                    String text = "";
                    text = text.concat(String.valueOf(n)).concat(";")
                            .concat(String.valueOf(k)).concat(";")
                            .concat(String.valueOf(javaTime)).concat(";")
                            .concat(String.valueOf(seqTime)).concat(";")
                            .concat(String.valueOf(parTime)).concat("\n");
                    myWriter.append(text);
                    myWriter.close();
                    System.out.println("Line added.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

            }
        }
    }
}
