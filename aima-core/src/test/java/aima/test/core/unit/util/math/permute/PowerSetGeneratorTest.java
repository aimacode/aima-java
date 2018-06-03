package aima.test.core.unit.util.math.permute;

import aima.core.util.math.permute.PowerSetGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author samagra
 */
public class PowerSetGeneratorTest {
    @Test
    public void powerSetGeneratorTest() {
        List<String> list = new ArrayList<>(Arrays.asList("alpha", "beta", "gamma"));
        int i = 0;
        for (List<String> strings :
                PowerSetGenerator.powerSet(list)) {
            i++;
        }
        Assert.assertEquals(7, i);
    }
}
