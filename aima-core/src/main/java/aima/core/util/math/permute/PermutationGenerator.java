package aima.core.util.math.permute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author samagra
 */
public class PermutationGenerator {

    public static <T> Iterable<List<T>> generatePermutations(List<T> list, int r) {
        long rfact = (long) PermutationGenerator.factorial(r);
        long total = (long) CombinationGenerator.nCr(list.size(), r) * rfact;
        return () -> new Iterator<List<T>>() {
            int index = -1;
            int permNo = 0;
            int[] currPermutation = new int[r];
            int[] currCombination = new int[r];

            @Override
            public boolean hasNext() {
                index++;
                return index < total;
            }

            @Override
            public List<T> next() {
                if (index == 0) {
                    permNo = 0;
                    for (int i = 0; i < currCombination.length; i++) {
                        currCombination[i] = i + 1;
                        currPermutation[i] = i + 1;
                    }

                } else if (((permNo + 1) % rfact) == 0) {
                    permNo++;
                    currCombination = CombinationGenerator.generateNextCombination(currCombination, list.size(), r);
                    for (int i = 0; i < currCombination.length; i++) {
                        currPermutation[i] = i + 1;
                    }
                } else {
                    permNo++;
                    currPermutation = PermutationGenerator.generateNextPermutation(currPermutation, r);
                }
                List<T> result = new ArrayList<>();
                for (int i = 0; i < r; i++) {
                    result.add(list.get(currCombination[currPermutation[i] - 1] - 1));
                }
                return result;
            }
        };
    }

    static double factorial(int n) {
        int nfact = 1;
        for (int i = 1; i <= n; i++) {
            nfact *= i;
        }
        return nfact;
    }

    public static int[] generateNextProduct(int[] curr, int[] max) {
        int n = curr.length - 1;
        curr[n]++;
        for (int i = n; i > 0; i--) {
            if (curr[i] > max[i]) {
                curr[i] = 1;
                curr[i - 1]++;
            }
        }
        return curr;
    }

    public static int[] generateNextPermutation(int[] temp, int n) {
        int m = n - 1;
        while (temp[m - 1] > temp[m])
            m--;
        int k = n;
        while (temp[m - 1] > temp[k - 1])
            k--;
        int swapVar;
        //swap m and k
        swapVar = temp[m - 1];
        temp[m - 1] = temp[k - 1];
        temp[k - 1] = swapVar;

        int p = m + 1;
        int q = n;
        while (p < q) {
            swapVar = temp[p - 1];
            temp[p - 1] = temp[q - 1];
            temp[q - 1] = swapVar;
            p++;
            q--;
        }
        return temp;
    }
    public static <T> Iterable<List<T>> product(List<T>... lists) {
        int total = 1;
        int[] max = new int[lists.length];
        for (int i = 0; i < lists.length; i++) {
            max[i] = lists[i].size();
        }
        int[] initProduct = new int[lists.length];
        Arrays.fill(initProduct, 1);
        for (List<T> list :
                lists) {
            total *= list.size();
        }
        int finalTotal = total;
        return () -> new Iterator<List<T>>() {
            int index = -1;
            int[] presentProduct;

            @Override
            public boolean hasNext() {
                index++;
                return index < finalTotal;
            }

            @Override
            public List<T> next() {
                if (index == 0)
                    presentProduct = initProduct;
                else
                    presentProduct = PermutationGenerator.generateNextProduct(presentProduct, max);
                List<T> result = new ArrayList<>();
                for (int i = 0; i < presentProduct.length; i++) {
                    result.add(lists[i].get(presentProduct[i] - 1));
                }
                return result;
            }
        };
    }
}
