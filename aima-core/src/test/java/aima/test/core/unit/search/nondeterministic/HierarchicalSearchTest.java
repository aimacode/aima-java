package aima.test.core.unit.search.nondeterministic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.nondeterministic.HierarchicalSearch;

/*
 TEST FOR HIERARCHICAL SEARCH 
 It currently tests the example given in the book.
 Author-Shubhankar Mohapatra
 */

class HierarchicalSearchTest extends HierarchicalSearch
{
	HierarchicalSearch object;
	@Before
	public void setUp()
	{
		this.object=new HierarchicalSearch();

	}

	@Test
	public void test1()
	{
		String correct[]={"Move Legs","Walk","Taxi Stand","Taxi","Get Inside Airport","San Fransisco Airport"};
		Assert.assertArrayEquals(correct,object.output);

	}
}