package aima.core.util.math.permute;

import java.util.ArrayList;
import java.util.List;

/**
 * @author samagra
 */
public class CombinationGenerator {
    public static <T> List<List<T>> generateCombinations(List<T> list, int r) {
        List<List<T>> result = new ArrayList<>();
        List<List<Integer>> mappings = combination(r, list.size());
        List<T> tempList;
        for (List<Integer> singleCombination :
                mappings) {
            tempList = new ArrayList<>();
            for (int a :
                    singleCombination) {
                tempList.add(list.get(a - 1));
            }
            result.add(tempList);
        }
        return result;
    }

    public static List<List<Integer>> combination(int r, int n) {
        int numberOfCombinations = (int) nCr(n, r);
        List<Integer> tempList;
        List<List<Integer>> result = new ArrayList<>();
        int[] temp = new int[r];
        for (int i = 0; i < r; i++) {
            temp[i] = i + 1;
        }
        tempList = new ArrayList<>();
        int x;
        for (int i = 0; i < r; i++) {
            x = temp[i];
            tempList.add(x);
        }
        result.add(tempList);
        int m, maxVal;
        for (int i = 1; i < numberOfCombinations; i++) {
            m = r;
            maxVal = n;
            while (temp[m - 1] == maxVal) {
                m = m - 1;
                maxVal--;
            }
            temp[m - 1]++;
            for (int j = m; j < r; j++) {
                temp[j] = temp[j - 1] + 1;
            }
            tempList = new ArrayList<>();
            for (int k = 0; k < r; k++) {
                x = temp[k];
                tempList.add(x);
            }
            result.add(tempList);
        }

        return result;
    }

    static double nCr(int n, int r) {
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
}
