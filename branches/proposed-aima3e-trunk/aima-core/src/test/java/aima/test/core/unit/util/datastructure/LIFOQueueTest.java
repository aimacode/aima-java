package aima.test.core.unit.util.datastructure;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.util.datastructure.LIFOQueue;

/**
 * @author Ravi Mohan
 * 
 */
public class LIFOQueueTest {

	@Test
	public void testLIFOQueue() {
		LIFOQueue<String> queue = new LIFOQueue<String>();
		Assert.assertTrue(queue.isEmpty());

		queue.add("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		queue.add("Hi");
		Assert.assertEquals(2, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.remove();
		Assert.assertEquals("Hi", s);
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("Hello", queue.peek());

		List<String> l = new ArrayList<String>();
		l.add("salaam alaikum");
		l.add("bonjour");
		queue.addAll(l);
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("salaam alaikum", queue.pop());
		Assert.assertEquals("bonjour", queue.pop());
		Assert.assertEquals("Hello", queue.pop());

		Assert.assertEquals(0, queue.size());
	}
}
