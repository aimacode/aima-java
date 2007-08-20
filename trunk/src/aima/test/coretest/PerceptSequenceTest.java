package aima.test.coretest;

import junit.framework.TestCase;
import aima.basic.Percept;
import aima.basic.PerceptSequence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class PerceptSequenceTest extends TestCase {

	public void testToString() {
		PerceptSequence ps = new PerceptSequence();
		ps.append(new Percept("key1", "value1"));

		assertEquals("[key1==value1]", ps.toString());

		ps.append(new Percept("key1", "value1", "key2", "value2"));

		assertEquals("[key1==value1], [key1==value1, key2==value2]", ps
				.toString());
	}

	public void testEquals() {
		PerceptSequence ps1 = new PerceptSequence();
		PerceptSequence ps2 = new PerceptSequence();

		assertEquals(ps1, ps2);

		ps1.append(new Percept("key1", "value1"));

		assertNotSame(ps1, ps2);

		ps2.append(new Percept("key1", "value1"));

		assertEquals(ps1, ps2);
	}

	public void testHashCode() {
		PerceptSequence ps = new PerceptSequence();

		assertEquals(0, ps.hashCode());

		ps.append(new Percept("key1", "value1"));

		assertEquals("[key1==value1]".hashCode(), ps.hashCode());
	}
}
