package aima.test.unit.search.basic;

import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;
import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BasicPriorityFrontierQueueTest<A, S> {
	
	private static NodeFactory<Integer, String> nodeFactory;
	private static int NUMBER_OF_NODES;
	private static int NUMBER_OF_STATES;
	private static Node[] testNodes;
	private static Node[] fastPQResult;
	private static Node[] normalPQResult;
	private static Comparator<Node<Integer, String>> comparator;
	
	@BeforeClass
	public static void prepare() {
		
		
		nodeFactory = new BasicNodeFactory<Integer, String>();
		
		//comparator to order elements
		
		comparator = Comparator
				.comparing(Node<Integer, String>::pathCost);	    
		
		// Large Test Case check		
		// Generating large random test case to compare runtimes
		
		NUMBER_OF_STATES = 500; 
		NUMBER_OF_NODES = 500000;		
		String[] states = new String[NUMBER_OF_STATES];
		for(int i = 0; i < states.length; ++i) {
			states[i] = "node"+i;
		}
		testNodes = new Node[NUMBER_OF_NODES];		
		for(int i = 0; i < NUMBER_OF_NODES; ++i) {
			double rand1 = Math.random();
			double rand2 = Math.random();
			testNodes[i] = nodeFactory.newRootNode(states[(int) (rand1*NUMBER_OF_STATES)], rand2);
		}
	}
	
	@Test
	public void smallTestsForBasicPriorityFrontierQueue() {
		// Small Test Cases for fastPQ
	    // TODO Need to add more small tests for other functions as well
		BasicPriorityFrontierQueue<Integer, String> fastPQ = new BasicPriorityFrontierQueue<Integer, String>(comparator);
		BasicFrontierQueue<Integer, String> normalPQ = new BasicFrontierQueue<Integer, String>(() -> new PriorityQueue<>(comparator), HashMap::new);
		
		// Defining 5 states root,node1,node2,node3 and node4
		
		Node<Integer, String> node = nodeFactory.newRootNode("root");		
		Node<Integer, String> node1 = nodeFactory.newRootNode("node1", 5);
		Node<Integer, String> node2 = nodeFactory.newRootNode("root", 0);
		Node<Integer, String> node3 = nodeFactory.newRootNode("node3", 9);
		Node<Integer, String> node4 = nodeFactory.newRootNode("node4", 6);
		
		fastPQ.add(node);
		fastPQ.add(node1);
		fastPQ.add(node2);
		fastPQ.add(node3);
		fastPQ.add(node4);
		normalPQ.add(node);
		normalPQ.add(node1);
		normalPQ.add(node2);
		normalPQ.add(node3);
		normalPQ.add(node4);
		// Checking for order correctness
		
		Assert.assertEquals(normalPQ.remove(), fastPQ.remove());
		Assert.assertEquals(normalPQ.remove(), fastPQ.remove());
		Assert.assertEquals(normalPQ.remove(), fastPQ.remove());
		Assert.assertEquals(normalPQ.remove(), fastPQ.remove());
		Assert.assertEquals(normalPQ.remove(), fastPQ.remove());
		
		//Checking for insertion of equal objects
		for(int i = 0; i < 10; ++i) {
			normalPQ.add(node);
			fastPQ.add(node);
		}
		Assert.assertEquals(normalPQ.size(), fastPQ.size());
		for(int i = 0; i < 10; ++i) {
			Assert.assertEquals(normalPQ.remove(), fastPQ.remove());
		}
	}
	
	@Test
	public void testBasicPriorityFrontierQueue() {	
		BasicPriorityFrontierQueue<Integer, String> fastPQ = new BasicPriorityFrontierQueue<Integer, String>(comparator, HashMap::new);
		//Adding all and removing from fast PQ
		
		for(int i = 0; i < NUMBER_OF_NODES; ++i) {
			fastPQ.add(testNodes[i]);
		}		
		fastPQResult = new Node[fastPQ.size()];
		for(int i = 0; i < fastPQResult.length; ++i) {
			fastPQResult[i] = fastPQ.remove();
		}
	}
	
	@Test
	public void testBasicFrontierQueue() {
		
		BasicFrontierQueue<Integer, String> normalPQ = new BasicFrontierQueue<Integer, String>(() -> new PriorityQueue<>(comparator), HashMap::new);
		
		//Adding all and removing from normal PQ		
		for(int i = 0; i < NUMBER_OF_NODES; ++i) {
			Node<Integer, String> tnode = testNodes[i];
			if(normalPQ.contains(testNodes[i].state())) {
				normalPQ.removeIf(n -> tnode.state().equals(n.state()) && (nodeFactory.getNodeComparator().compare(tnode, n) < 0));
				if(!normalPQ.contains(testNodes[i])) {
					normalPQ.add(testNodes[i]);
				}
			}
			else {
				normalPQ.add(testNodes[i]);
			}
		}
		normalPQResult = new Node[normalPQ.size()];		
		for(int i = 0; i < normalPQResult.length; ++i) {
			normalPQResult[i] = normalPQ.remove();
		}
	}
	
	@AfterClass
	public static void finalCheck() {
		Assert.assertEquals(normalPQResult.length, fastPQResult.length);
		for(int i = 0; i < fastPQResult.length; ++i) {			
			Assert.assertEquals(normalPQResult[i], fastPQResult[i]);
		}		
	}

}
