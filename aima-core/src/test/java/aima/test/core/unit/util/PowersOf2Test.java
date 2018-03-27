package aima.test.core.unit.util;

import aima.core.util.PowersOf2;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * @author Frederik Andersen
 *
 */
public class PowersOf2Test {

    @Test
    public void testIteration() {
        PowersOf2 iter = new PowersOf2();
        Iterator<Integer> generator = iter.iterator();
        Assert.assertTrue(generator.hasNext());

        Assert.assertEquals(1, (int)generator.next());
        Assert.assertEquals(2, (int)generator.next());
        Assert.assertEquals(4, (int)generator.next());
        Assert.assertEquals(8, (int)generator.next());
    }
}
