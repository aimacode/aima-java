package aima.test.search;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.datastructures.FIFOQueue;

/**
 * @author Ravi Mohan
 * 
 */

public class FIFOQueueTest extends TestCase {

	public void testFifoQueue() {
		FIFOQueue queue = new FIFOQueue();
		assertTrue(queue.isEmpty());

		queue.add("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		queue.add("Hi");
		assertEquals(2, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.remove();
		assertEquals("Hello", s);
		assertEquals(1, queue.size());
		assertEquals("Hi", queue.get());

		List<String> l = new ArrayList<String>();
		l.add("bonjour");
		l.add("salaam alaikum");
		queue.add(l);
		assertEquals(3, queue.size());
		assertEquals("Hi", queue.remove());
		assertEquals("bonjour", queue.get());

		queue.add(l);

	}

}
