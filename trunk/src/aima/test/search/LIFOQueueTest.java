package aima.test.search;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.datastructures.LIFOQueue;

/**
 * @author Ravi Mohan
 * 
 */

public class LIFOQueueTest extends TestCase {
	public void testFifoQueue() {
		LIFOQueue queue = new LIFOQueue();
		assertTrue(queue.isEmpty());

		queue.add("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		queue.add("Hi");
		assertEquals(2, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.remove();
		assertEquals("Hi", s);
		assertEquals(1, queue.size());
		assertEquals("Hello", queue.get());

		List<String> l = new ArrayList<String>();
		l.add("bonjour");
		l.add("salaam alaikum");
		queue.add(l);
		assertEquals(3, queue.size());
		assertEquals("salaam alaikum", queue.get());

		queue.add(l);

	}
}
