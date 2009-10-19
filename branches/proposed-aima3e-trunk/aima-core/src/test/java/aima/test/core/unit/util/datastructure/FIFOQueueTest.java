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
	public void testFifoQueue() {
		FIFOQueue queue = new FIFOQueue();
		Assert.assertTrue(queue.isEmpty());

		queue.add("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		queue.add("Hi");
		Assert.assertEquals(2, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.remove();
		Assert.assertEquals("Hello", s);
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("Hi", queue.get());

		List<String> l = new ArrayList<String>();
		l.add("bonjour");
		l.add("salaam alaikum");
		queue.add(l);
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("Hi", queue.remove());
		Assert.assertEquals("bonjour", queue.get());

		queue.add(l);
	}
}
