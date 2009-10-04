/*
 * Created on Sep 20, 2004
 *
 */
package aima.test.core.unit.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */

public class SetTest  {
	Set<Integer> s1, s2;

	@Before
	public void setUp() {
		s1 = new HashSet<Integer>();
		s1.add(new Integer(1));
		s1.add(new Integer(2));
		s1.add(new Integer(3));
		s1.add(new Integer(4));

		s2 = new HashSet<Integer>();
		s2.add(new Integer(4));
		s2.add(new Integer(5));
		s2.add(new Integer(6));
	}

	@Test
	public void testUnion() {
		Set<Integer> union = new SetOps<Integer>().union(s1, s2);
		Assert.assertEquals(6, union.size());
		Assert.assertEquals(4, s1.size());
		Assert.assertEquals(3, s2.size());

		s1.remove(new Integer(1));
		Assert.assertEquals(6, union.size());
		Assert.assertEquals(3, s1.size());
		Assert.assertEquals(3, s2.size());
	}

	@Test
	public void testIntersection() {
		Set<Integer> intersection = new SetOps<Integer>().intersection(s1, s2);
		Assert.assertEquals(1, intersection.size());
		Assert.assertEquals(4, s1.size());
		Assert.assertEquals(3, s2.size());

		s1.remove(new Integer(1));
		Assert.assertEquals(1, intersection.size());
		Assert.assertEquals(3, s1.size());
		Assert.assertEquals(3, s2.size());
	}

	@Test
	public void testDifference() {
		Set<Integer> difference = new SetOps<Integer>().difference(s1, s2);
		Assert.assertEquals(3, difference.size());
		Assert.assertTrue(difference.contains(new Integer(1)));
		Assert.assertTrue(difference.contains(new Integer(2)));
		Assert.assertTrue(difference.contains(new Integer(3)));
	}

	@Test
	public void testDifference2() {
		Set<Integer> one = new HashSet<Integer>();
		Set<Integer> two = new HashSet<Integer>();
		one.add(new Integer(1));
		two.add(new Integer(1));
		Set difference = new SetOps<Integer>().difference(one, two);
		Assert.assertTrue(difference.isEmpty());
	}

	@Test
	public void testListRemove() {
		List<Integer> one = new ArrayList<Integer>();
		one.add(new Integer(1));
		Assert.assertEquals(1, one.size());
		one.remove(0);
		Assert.assertEquals(0, one.size());
	}
}