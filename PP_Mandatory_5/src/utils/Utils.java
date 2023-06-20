package utils;

import precode.IntList;

import java.util.Arrays;
import java.util.Comparator;

public class Utils {
    static public int[] lineEquation(int[] x, int[] y, int p1, int p2) {
        int x1, x2, y1, y2;
        int a, b, c;

        x1 = x[p1];
        x2 = x[p2];

        y1 = y[p1];
        y2 = y[p2];

        a = y1 - y2;
        b = x2 - x1;
        c = y2 * x1 - y1 * x2;

        return new int[]{a, b, c};
    }

    static public Integer[] copy(IntList list) {
        Integer[] array = new Integer[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static int distance(int[] x, int[] y, int p1, int p2) {
        return (int) (Math.pow(x[p1] - x[p2], 2) + Math.pow(y[p1] - y[p2], 2));
    }

    static public void sortLines(int[] x, int[] y, IntList listLines, int p2) {
        Integer[] arrayLines = copy(listLines);
        Arrays.sort(arrayLines,
                0,
                listLines.size(),
                (Comparator.comparingInt((Integer i) -> distance(x, y, i, p2)))
        );

        for (int i = 0; i < arrayLines.length; i++) {
            listLines.add(arrayLines[i], i);
        }
    }
}
