package aima.test.core.unit.util;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import aima.core.util.DisjointSets;

public class DisjointSetsTest {
	
	@Test
	public void testConstructors() {
		DisjointSets<String> disjSets = new DisjointSets<String>();
		Assert.assertEquals(0, disjSets.numberDisjointSets());
		
		disjSets = new DisjointSets<String>("a", "a", "b");
		Assert.assertEquals(2, disjSets.numberDisjointSets());
		
		disjSets = new DisjointSets<String>(Arrays.asList("a", "a", "b"));
		Assert.assertEquals(2, disjSets.numberDisjointSets());
	}
	
	@Test
	public void testMakeSet() {
		DisjointSets<String> disjSets = new DisjointSets<String>();
		
		disjSets.makeSet("a");
		Assert.assertEquals(1, disjSets.numberDisjointSets());
		
		disjSets.makeSet("a");
		Assert.assertEquals(1, disjSets.numberDisjointSets());
		
		disjSets.makeSet("b");
		Assert.assertEquals(2, disjSets.numberDisjointSets());
	}
	
	@Test
	public void testUnion() {
		DisjointSets<String> disjSets = new DisjointSets<String>(
				"a", "b", "c", "d");
		Assert.assertEquals(4, disjSets.numberDisjointSets());
		
		disjSets.union("a", "b");
		Assert.assertEquals(3, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("a"), disjSets.find("b"));
		
		disjSets.union("c", "d");
		Assert.assertEquals(2, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("c"), disjSets.find("d"));
		
		disjSets.union("b", "c");
		Assert.assertEquals(1, disjSets.numberDisjointSets());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnionIllegalArgumentException1() {
		DisjointSets<String> disjSets = new DisjointSets<String>(
				"a");
		disjSets.union("b", "a");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnionIllegalArgumentException2() {
		DisjointSets<String> disjSets = new DisjointSets<String>(
				"a");
		disjSets.union("a", "b");
	}

	/**
	 * Note: This is based on the example given in Figure 21.1 of 'Introduction
	 * to Algorithm 2nd Edition' (by Cormen, Leriserson, Rivest, and Stein)
	 */
	@Test
	public void testWorkedExample() {
		// Should be the following when finished:
		// {a, b, c, d}, {e, f, g}, {h, i}, and {j}
		
		// 1. initial sets
		DisjointSets<String> disjSets = new DisjointSets<String>(
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
		
		Assert.assertEquals(10, disjSets.numberDisjointSets());
		Assert.assertEquals(1, disjSets.find("a").size());
		Assert.assertEquals(1, disjSets.find("b").size());
		Assert.assertEquals(1, disjSets.find("c").size());
		Assert.assertEquals(1, disjSets.find("d").size());
		Assert.assertEquals(1, disjSets.find("e").size());
		Assert.assertEquals(1, disjSets.find("f").size());
		Assert.assertEquals(1, disjSets.find("g").size());
		Assert.assertEquals(1, disjSets.find("h").size());
		Assert.assertEquals(1, disjSets.find("i").size());
		Assert.assertEquals(1, disjSets.find("j").size());
		
		// 2. (b, d)
		disjSets.union("b", "d");
		Assert.assertEquals(9, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("b"), disjSets.find("d"));
		
		// 3. (e, g)
		disjSets.union("e", "g");		
		Assert.assertEquals(8, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("e"), disjSets.find("g"));
		
		// 4. (a, c)
		disjSets.union("a", "c");		
		Assert.assertEquals(7, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("a"), disjSets.find("c"));
		
		// 5. (h, i)
		disjSets.union("h", "i");		
		Assert.assertEquals(6, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("h"), disjSets.find("i"));
		
		// 6. (a, b)
		disjSets.union("a", "b");
		Assert.assertEquals(5, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("a"), disjSets.find("b"));
		Assert.assertEquals(disjSets.find("b"), disjSets.find("c"));
		Assert.assertEquals(disjSets.find("c"), disjSets.find("d"));
		
		// 7. (e, f)
		disjSets.union("e", "f");
		Assert.assertEquals(4, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("e"), disjSets.find("f"));
		Assert.assertEquals(disjSets.find("f"), disjSets.find("g"));
		
		// 8. (b, c)
		disjSets.union("b", "c");
		Assert.assertEquals(4, disjSets.numberDisjointSets());
		Assert.assertEquals(disjSets.find("a"), disjSets.find("b"));
		Assert.assertEquals(disjSets.find("b"), disjSets.find("c"));
		Assert.assertEquals(disjSets.find("c"), disjSets.find("d"));
	}
}
