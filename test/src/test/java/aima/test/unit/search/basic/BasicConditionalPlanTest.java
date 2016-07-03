package aima.test.unit.search.basic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.search.api.ConditionalPlan;
import aima.core.search.basic.support.BasicConditionalPlan;
import aima.core.util.datastructure.Pair;

public class BasicConditionalPlanTest {
	@Test
	public void testBasicConfitionalPlan() {
		BasicConditionalPlan<String, Integer> cp = new BasicConditionalPlan<>();
		Assert.assertEquals("[]", cp.toString());

		cp = new BasicConditionalPlan<>("Suck", cp);
		Assert.assertEquals("[Suck]", cp.toString());

		cp = new BasicConditionalPlan<>("Right", cp);
		Assert.assertEquals("[Right, Suck]", cp.toString());

		List<Pair<Integer, ConditionalPlan<String, Integer>>> cps = new ArrayList<>();
		cps.add(new Pair<>(5, cp));
		cps.add(new Pair<>(7, new BasicConditionalPlan<>()));
		cp = new BasicConditionalPlan<>(cps);
		Assert.assertEquals("[if State = 5 then [Right, Suck] else []]", cp.toString());

		cp = new BasicConditionalPlan<>("Suck", cp);
		Assert.assertEquals("[Suck, if State = 5 then [Right, Suck] else []]", cp.toString());
	}
}
