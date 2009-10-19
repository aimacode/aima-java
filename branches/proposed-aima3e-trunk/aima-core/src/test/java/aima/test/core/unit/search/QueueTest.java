package aima.test.core.unit.search;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.util.datastructure.DefaultQueue;

/**
 * @author Ravi Mohan
 * 
 */
public class QueueTest {

	private DefaultQueue queue;

	@Before
	public void setUp() {
		queue = new DefaultQueue();
	}

	@Test
	public void testInitialization() {
		Assert.assertEquals(0, queue.size());
		Assert.assertTrue(queue.isEmpty());
	}

	@Test
	public void testGetFirst() {
		queue.addToFront("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.getFirst();
		Assert.assertEquals("Hello", s);
		Assert.assertFalse(queue.isEmpty());
	}

	@Test
	public void testRemoveFirst() {
		queue.addToFront("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.removeFirst();
		Assert.assertEquals("Hello", s);
		Assert.assertTrue(queue.isEmpty());
	}

	@Test
	public void testGetLast() {
		queue.addToFront("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.getLast();
		Assert.assertEquals("Hello", s);
		Assert.assertFalse(queue.isEmpty());
	}

	@Test
	public void testRemoveLast() {
		queue.addToFront("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.removeLast();
		Assert.assertEquals("Hello", s);
		Assert.assertTrue(queue.isEmpty());
	}

	@Test
	public void testAddToFront() {
		queue.addToFront("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		queue.addToFront("Hi");
		Assert.assertEquals(2, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.removeFirst();
		Assert.assertEquals("Hi", s);
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("Hello", queue.getFirst());
	}

	@Test
	public void testAddToBack() {
		queue.addToBack("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		queue.addToBack("Hi");
		Assert.assertEquals(2, queue.size());
		Assert.assertFalse(queue.isEmpty());

		String s = (String) queue.removeFirst();
		Assert.assertEquals("Hello", s);
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("Hi", queue.getFirst());
	}

	@Test
	public void testMultipleAddToFront() {
		queue.addToFront("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		List<String> l = new ArrayList<String>();
		l.add("Hi");
		l.add("Bye");
		queue.addToFront(l);
		Assert.assertEquals(3, queue.size());

		String s = (String) queue.removeFirst();
		Assert.assertEquals("Hi", s);
		Assert.assertEquals(2, queue.size());
		Assert.assertEquals("Bye", queue.getFirst());
	}

	@Test
	public void testMultipleAddToBack() {
		queue.addToFront("Hello");
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.isEmpty());

		List<String> l = new ArrayList<String>();
		l.add("Hi");
		l.add("Bye");
		queue.addToBack(l);
		Assert.assertEquals(3, queue.size());

		String s = (String) queue.removeFirst();
		Assert.assertEquals("Hello", s);
		Assert.assertEquals(2, queue.size());
		Assert.assertEquals("Hi", queue.getFirst());
	}

	@Test
	public void testAsList() {
		queue.addToFront("Hello");
		List<String> l = new ArrayList<String>();
		l.add("Hi");
		l.add("Bye");
		queue.addToBack(l);
		Assert.assertEquals(3, queue.size());
		List list = queue.asList();
		Assert.assertEquals(3, list.size());
	}

	@Test
	public void testExceptions() {
		try {
			queue.add("Hello");
			Assert.fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}

		try {
			queue.remove();
			Assert.fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}
		try {
			queue.get();
			Assert.fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}
		List l = new ArrayList();
		try {
			queue.add(l);
			Assert.fail("Runtime Exception should have been thrown");
		} catch (RuntimeException e) {
			// Test passed
		}
	}
}
