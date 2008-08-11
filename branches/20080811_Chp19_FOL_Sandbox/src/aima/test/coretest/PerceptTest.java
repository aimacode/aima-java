package aima.test.coretest;

import aima.basic.Percept;
import junit.framework.TestCase;

public class PerceptTest extends TestCase {

	public void testToString() {
		Percept p = new Percept("key1", "value1");

		assertEquals("[key1==value1]", p.toString());

		p = new Percept("key1", "value1", "key2", "value2");

		assertEquals("[key1==value1, key2==value2]", p.toString());
	}

	public void testEquals() {
		Percept p1 = new Percept();
		Percept p2 = new Percept();

		assertEquals(p1, p2);

		p1 = new Percept("key1", "value1");

		assertNotSame(p1, p2);

		p2 = new Percept("key1", "value1");

		assertEquals(p1, p2);
	}

	public void testHashCode() {
		Percept p = new Percept();

		assertEquals(0, p.hashCode());

		p = new Percept("key1", "value1");

		assertEquals("[key1==value1]".hashCode(), p.hashCode());
	}
}
