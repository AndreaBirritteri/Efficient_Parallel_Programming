package com.company;

import static com.company.Utils.insertSortDesc;

public class Sequential {

    static public void sequentialWebSearchSort(int[] array, int k) {
        int n = array.length;

        insertSortDesc(array, 0, k - 1);

        for (int j = k; j <= n - 1; j++) {
            if (array[j] > array[k - 1]) {
                array[k - 1] = array[j];
                int switchCount = k - 2;
                while (switchCount >= 0 && array[switchCount] < array[switchCount + 1]) {
                    int supp = array[switchCount];
                    array[switchCount] = array[switchCount + 1];
                    array[switchCount + 1] = supp;
                    switchCount--;
                }
                //insertSortDesc(array, 0, k - 1);
            }
        }
    }
}
