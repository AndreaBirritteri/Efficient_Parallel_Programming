package com.company;

public class Utils {
    static void insertSortDesc(int[] array, int startIndex, int finishIndex) {
        int thisIndex, thisNumber;
        for (int indexes = startIndex; indexes < finishIndex; indexes++) {
            thisNumber = array[indexes + 1];
            thisIndex = indexes;
            while (thisIndex >= startIndex && array[thisIndex] < thisNumber) {
                array[thisIndex + 1] = array[thisIndex];
                thisIndex--;
            }
            array[thisIndex + 1] = thisNumber;
        }
    }
}
