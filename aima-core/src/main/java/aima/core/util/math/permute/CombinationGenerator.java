package aima.core.util.math.permute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author samagra
 */
public class CombinationGenerator {
    public static <T> Iterable<List<T>> generateCombinations(List<T> list, int r) {
        return () -> new Iterator<List<T>>() {
            int index = -1;
            int total = (int) CombinationGenerator.nCr(list.size(), r);
            int[] currCombination = new int[r];

            @Override
            public boolean hasNext() {
                index++;
                return index < total;
            }

            @Override
            public List<T> next() {
                if (index == 0) {
                    for (int i = 0; i < currCombination.length; i++) {
                        currCombination[i] = i + 1;
                    }
                } else
                    currCombination = CombinationGenerator.generateNextCombination(currCombination, list.size(), r);
                List<T> result = new ArrayList<>();
                for (int aCurrCombination : currCombination) {
                    result.add(list.get(aCurrCombination - 1));
                }
                return result;
            }
        };
    }

    public static double nCr(int n, int r) {
        int rfact = 1, nfact = 1, nrfact = 1, temp1 = n - r, temp2 = r;
        if (r > n - r) {
            temp1 = r;
            temp2 = n - r;
        }
        for (int i = 1; i <= n; i++) {
            if (i <= temp2) {
                rfact *= i;
                nrfact *= i;
            } else if (i <= temp1) {
                nrfact *= i;
            }
            nfact *= i;
        }
        return nfact / (double) (rfact * nrfact);
    }

    public static int[] generateNextCombination(int[] temp, int n, int r) {
        int m = r;
        int maxVal = n;
        while (temp[m - 1] == maxVal) {
            m = m - 1;
            maxVal--;
        }
        temp[m - 1]++;
        for (int j = m; j < r; j++) {
            temp[j] = temp[j - 1] + 1;
        }
        return temp;
    }
}
