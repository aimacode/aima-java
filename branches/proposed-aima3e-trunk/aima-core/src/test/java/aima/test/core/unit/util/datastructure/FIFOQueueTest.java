package aima.test.core.unit.util.datastructure;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.util.datastructure.FIFOQueue;

/**
 * @author Ravi Mohan
 * 
 */
public class FIFOQueueTest {

	@Test
	public void testFIFOQueue() {
		FIFOQueue<String> queue = new FIFOQueue<String>();
		Assert.assertTrue(queue.isEmpty());

		queue.add("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		queue.add("Hi");
		Assert.assertEquals(2, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = queue.remove();
		Assert.assertEquals("Hello", s);
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("Hi", queue.peek());

		List<String> l = new ArrayList<String>();
		l.add("bonjour");
		l.add("salaam alaikum");
		queue.addAll(l);
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("Hi", queue.pop());
		Assert.assertEquals("bonjour", queue.pop());
		Assert.assertEquals("salaam alaikum", queue.pop());

		Assert.assertEquals(0, queue.size());
	}
}
