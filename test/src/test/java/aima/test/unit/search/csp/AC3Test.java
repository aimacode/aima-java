package aima.test.unit.search.csp;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import aima.core.search.api.CSP;
import aima.core.search.basic.csp.AC3;
import aima.core.search.basic.support.BasicCSP;
import aima.core.search.basic.support.BasicConstraint;

public class AC3Test {

	public AC3 ac3 = new AC3();

	@Test
	public void testBasicAC3() {
		Object[] digits = new Object[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		CSP csp = new BasicCSP(new String[] { "X", "Y" }, new Object[][] { digits, digits },
				BasicConstraint.newTabularConstraint(new String[] { "X", "Y" },
						new Object[][] { { 0, 0 }, { 1, 1 }, { 2, 4 }, { 3, 9 } }));

		Assert.assertEquals(Arrays.asList("X", "Y"), csp.getVariables());
		Assert.assertEquals(0, csp.indexOf("X"));
		Assert.assertEquals(1, csp.indexOf("Y"));
		Assert.assertEquals(10, csp.getDomains().get(csp.indexOf("X")).size());
		Assert.assertEquals(10, csp.getDomains().get(csp.indexOf("Y")).size());

		Assert.assertTrue(ac3.test(csp));
		Assert.assertEquals(Arrays.asList("X", "Y"), csp.getVariables());
		Assert.assertEquals(4, csp.getDomains().get(0).size());
		Assert.assertEquals(Arrays.asList(0, 1, 2, 3), csp.getDomains().get(csp.indexOf("X")).getValues());
		Assert.assertEquals(4, csp.getDomains().get(csp.indexOf("Y")).size());
		Assert.assertEquals(Arrays.asList(0, 1, 4, 9), csp.getDomains().get(csp.indexOf("Y")).getValues());

		csp = new BasicCSP(new String[] { "X", "Y" }, new Object[][] { { 0, 2, 4 }, { 0, 1, 2, 3, 4, 5 } },
				new BasicConstraint(new String[] { "X", "Y" },
						values -> ((Integer) values[0]) + ((Integer) values[1]) == 4));

		Assert.assertEquals(Arrays.asList("X", "Y"), csp.getVariables());
		Assert.assertEquals(0, csp.indexOf("X"));
		Assert.assertEquals(1, csp.indexOf("Y"));
		Assert.assertEquals(3, csp.getDomains().get(csp.indexOf("X")).size());
		Assert.assertEquals(6, csp.getDomains().get(csp.indexOf("Y")).size());

		Assert.assertTrue(ac3.test(csp));
		Assert.assertEquals(Arrays.asList("X", "Y"), csp.getVariables());
		Assert.assertEquals(3, csp.getDomains().get(0).size());
		Assert.assertEquals(Arrays.asList(0, 2, 4), csp.getDomains().get(csp.indexOf("X")).getValues());
		Assert.assertEquals(3, csp.getDomains().get(csp.indexOf("Y")).size());
		Assert.assertEquals(Arrays.asList(0, 2, 4), csp.getDomains().get(csp.indexOf("Y")).getValues());
	}
}
