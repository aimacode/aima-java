package aima.core.util;

import java.util.Iterator;

/**
 * Java has standard interface for implementing iterators. The "foreach"
 * construction makes it easy to loop over objects that provide the
 * java.lang.Iterable interface. However Java does not have generators built
 * into the language. This means that even a simple iterator has a lot of
 * boilerplate code.
 *
 * @author Frederik Andersen
 */
public class PowersOf2 implements Iterable<Integer> {

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int i = 1;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                int old = i;
                i = 2 * i;
                return old;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        // Example of using the "foreach" construction. This never stops.
        for (int p : new PowersOf2()) {
            System.out.println(p);
        }
    }
}
