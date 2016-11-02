package aima.test.core.unit.search.framework;

import aima.core.search.framework.Metrics;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class MetricsTest {

    private Metrics metrics;

    @Before
    public void before() {
        metrics = new Metrics();
    }

    @Test
    public void testGetInt() {
        int x = 893597823;
        metrics.set("abcd",x);
        assertEquals(x, metrics.getInt("abcd"));
        assertNotEquals(1234,metrics.getInt("abcd"));
    }

    @Test
    public void testGetDouble() {
        double x = 1231397235234.48467865326;
        metrics.set("abcd",x);
        assertEquals(x, metrics.getDouble("abcd"),0);
        assertNotEquals(1234.56789,metrics.getDouble("abcd"),0);
    }

    @Test
    public void testGetLong() {
        long x = 893597823;
        metrics.set("abcd",x);
        assertEquals(x, metrics.getLong("abcd"));
        assertNotEquals(841356458,metrics.getLong("abcd"));
    }

    @Test
    public void testGet() {
        int x = 123;
        metrics.set("abcd",x);
        assertEquals("123", metrics.get("abcd"));
        assertNotEquals("1234",metrics.get("abcd"));
    }

}
