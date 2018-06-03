package aima.core.util.math.permute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is used to generate the powerset of a given list of items.
 * @author samagra
 */
public class PowerSetGenerator {
    public static <T> Iterable<List<T>> powerSet(List<T> list){
        int total = (int) Math.pow(2,list.size());
        return () -> new Iterator<List<T>>() {
            int count = 0;
            @Override
            public boolean hasNext() {
                count++;
                return count<total;
            }

            @Override
            public List<T> next() {
                String binaryString = Integer.toBinaryString(count);
                List<T> result = new ArrayList<>();
                for (int i = 0; i <binaryString.length(); i++) {
                    if (binaryString.charAt(binaryString.length()-i-1)=='1') {
                        result.add(list.get(i));
                    }
                }
                return result;
            }
        };
    }
}
