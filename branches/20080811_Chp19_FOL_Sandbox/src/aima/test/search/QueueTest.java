package aima.test.search;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.util.AbstractQueue;

/**
 * @author Ravi Mohan
 * 
 */
public class QueueTest extends TestCase {

	private AbstractQueue queue;

	@Override
	public void setUp() {
		queue = new AbstractQueue();
	}

	public void testInitialization() {
		assertEquals(0, queue.size());
		assertTrue(queue.isEmpty());
	}

	public void testGetFirst() {
		queue.addToFront("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.getFirst();
		assertEquals("Hello", s);
		assertFalse(queue.isEmpty());
	}

	public void testRemoveFirst() {
		queue.addToFront("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.removeFirst();
		assertEquals("Hello", s);
		assertTrue(queue.isEmpty());
	}

	public void testGetLast() {
		queue.addToFront("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.getLast();
		assertEquals("Hello", s);
		assertFalse(queue.isEmpty());
	}

	public void testRemoveLast() {
		queue.addToFront("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.removeLast();
		assertEquals("Hello", s);
		assertTrue(queue.isEmpty());
	}

	public void testAddToFront() {
		queue.addToFront("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		queue.addToFront("Hi");
		assertEquals(2, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.removeFirst();
		assertEquals("Hi", s);
		assertEquals(1, queue.size());
		assertEquals("Hello", queue.getFirst());

	}

	public void testAddToBack() {
		queue.addToBack("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		queue.addToBack("Hi");
		assertEquals(2, queue.size());
		assertFalse(queue.isEmpty());

		String s = (String) queue.removeFirst();
		assertEquals("Hello", s);
		assertEquals(1, queue.size());
		assertEquals("Hi", queue.getFirst());

	}

	public void testMultipleAddToFront() {
		queue.addToFront("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		List<String> l = new ArrayList<String>();
		l.add("Hi");
		l.add("Bye");
		queue.addToFront(l);
		assertEquals(3, queue.size());

		String s = (String) queue.removeFirst();
		assertEquals("Hi", s);
		assertEquals(2, queue.size());
		assertEquals("Bye", queue.getFirst());

	}

	public void testMultipleAddToBack() {
		queue.addToFront("Hello");
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());

		List<String> l = new ArrayList<String>();
		l.add("Hi");
		l.add("Bye");
		queue.addToBack(l);
		assertEquals(3, queue.size());

		String s = (String) queue.removeFirst();
		assertEquals("Hello", s);
		assertEquals(2, queue.size());
		assertEquals("Hi", queue.getFirst());

	}

	public void testAsList() {
		queue.addToFront("Hello");
		List<String> l = new ArrayList<String>();
		l.add("Hi");
		l.add("Bye");
		queue.addToBack(l);
		assertEquals(3, queue.size());
		List list = queue.asList();
		assertEquals(3, list.size());

	}

	public void testExceptions() {
		try {
			queue.add("Hello");
			fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}

		try {
			queue.remove();
			fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}
		try {
			queue.get();
			fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}
		List l = new ArrayList();
		try {
			queue.add(l);
			fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}

	}
}
