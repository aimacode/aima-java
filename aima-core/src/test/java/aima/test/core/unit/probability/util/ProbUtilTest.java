package aima.test.core.unit.probability.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.RandomVariable;
import aima.core.probability.domain.ArbitraryTokenDomain;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.RandVar;

public class ProbUtilTest {

	@Test
	public void test_indexOf() {
		RandVar X = new RandVar("X", new BooleanDomain());
		RandVar Y = new RandVar("Y", new ArbitraryTokenDomain("A", "B", "C"));
		RandVar Z = new RandVar("Z", new BooleanDomain());

		// An ordered X,Y,Z enumeration of values should look like:
		// 00: true, A, true
		// 01: true, A, false
		// 02: true, B, true
		// 03: true, B, false
		// 04: true, C, true
		// 05: true, C, false
		// 06: false, A, true
		// 07: false, A, false
		// 08: false, B, true
		// 09: false, B, false
		// 10: false, C, true
		// 11: false, C, false
		RandomVariable[] vars = new RandomVariable[] { X, Y, Z };
		Map<RandomVariable, Object> event = new LinkedHashMap<RandomVariable, Object>();

		event.put(X, Boolean.TRUE);
		event.put(Y, "A");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(0, ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(1, ProbUtil.indexOf(vars, event));
		event.put(Y, "B");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(2, ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(3, ProbUtil.indexOf(vars, event));
		event.put(Y, "C");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(4, ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(5, ProbUtil.indexOf(vars, event));
		//
		event.put(X, Boolean.FALSE);
		event.put(Y, "A");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(6, ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(7, ProbUtil.indexOf(vars, event));
		event.put(Y, "B");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(8, ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(9, ProbUtil.indexOf(vars, event));
		event.put(Y, "C");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(10, ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(11, ProbUtil.indexOf(vars, event));
	}

	@Test
	public void test_indexesOfValue() {
		RandVar X = new RandVar("X", new BooleanDomain());
		RandVar Y = new RandVar("Y", new ArbitraryTokenDomain("A", "B", "C"));
		RandVar Z = new RandVar("Z", new BooleanDomain());

		// An ordered X,Y,Z enumeration of values should look like:
		// 00: true, A, true
		// 01: true, A, false
		// 02: true, B, true
		// 03: true, B, false
		// 04: true, C, true
		// 05: true, C, false
		// 06: false, A, true
		// 07: false, A, false
		// 08: false, B, true
		// 09: false, B, false
		// 10: false, C, true
		// 11: false, C, false
		RandomVariable[] vars = new RandomVariable[] { X, Y, Z };
		Map<RandomVariable, Object> event = new LinkedHashMap<RandomVariable, Object>();

		event.put(X, Boolean.TRUE);
		Assert.assertArrayEquals(new int[] { 0, 1, 2, 3, 4, 5 },
				ProbUtil.indexesOfValue(vars, 0, event));
		event.put(X, Boolean.FALSE);
		Assert.assertArrayEquals(new int[] { 6, 7, 8, 9, 10, 11 },
				ProbUtil.indexesOfValue(vars, 0, event));

		event.put(Y, "A");
		Assert.assertArrayEquals(new int[] { 0, 1, 6, 7 },
				ProbUtil.indexesOfValue(vars, 1, event));
		event.put(Y, "B");
		Assert.assertArrayEquals(new int[] { 2, 3, 8, 9 },
				ProbUtil.indexesOfValue(vars, 1, event));
		event.put(Y, "C");
		Assert.assertArrayEquals(new int[] { 4, 5, 10, 11 },
				ProbUtil.indexesOfValue(vars, 1, event));

		event.put(Z, Boolean.TRUE);
		Assert.assertArrayEquals(new int[] { 0, 2, 4, 6, 8, 10 },
				ProbUtil.indexesOfValue(vars, 2, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertArrayEquals(new int[] { 1, 3, 5, 7, 9, 11 },
				ProbUtil.indexesOfValue(vars, 2, event));
	}
}
