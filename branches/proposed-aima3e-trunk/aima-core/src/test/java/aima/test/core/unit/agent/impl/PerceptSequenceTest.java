package aima.test.core.unit.agent.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicPercept;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class PerceptSequenceTest {

	@Test
	public void testToString() {
		List<Percept> ps = new ArrayList<Percept>();
		ps.add(new DynamicPercept("key1", "value1"));

		Assert.assertEquals("[Percept[key1==value1]]", ps.toString());

		ps.add(new DynamicPercept("key1", "value1", "key2", "value2"));

		Assert.assertEquals(
				"[Percept[key1==value1], Percept[key1==value1, key2==value2]]",
				ps.toString());
	}

	@Test
	public void testEquals() {
		List<Percept> ps1 = new ArrayList<Percept>();
		List<Percept> ps2 = new ArrayList<Percept>();

		Assert.assertEquals(ps1, ps2);

		ps1.add(new DynamicPercept("key1", "value1"));

		Assert.assertNotSame(ps1, ps2);

		ps2.add(new DynamicPercept("key1", "value1"));

		Assert.assertEquals(ps1, ps2);
	}
}
