package aima.test.core.unit.search.nondeterministic;

import java.io.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import aima.core.search.nondeterministic.*;

public class HierarchicalSearchTest extends HierarchicalSearch
{
	 HierarchicalSearch object;
	@Before
	public void setUp()throws IOException
	{
		this.object=new HierarchicalSearch();
		object.set();

	}

	@Test
	public void test1()
	{
		String correct[]={"Move Legs","Walk","Taxi Stand","Taxi","Get Inside Airport","San Fransisco Airport","","","",""};
		Assert.assertArrayEquals(correct,object.output);

	}
}