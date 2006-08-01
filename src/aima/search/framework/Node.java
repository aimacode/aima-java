package aima.search.framework;

import java.util.Hashtable;
import java.util.List;

import aima.util.AbstractQueue;

public class Node {

	private Object state;

	private Node parent;

	private Hashtable actionStateMap;

	private String action;

	private int depth;

	private Double stepCost, pathCost;

	public Node(Object state) {

		this.state = state;
		this.actionStateMap = new Hashtable();
		this.depth = 0;
		this.stepCost = new Double(0);
		this.pathCost = new Double(0);
	}

	public Node(Node parent, Object state) {
		this(state);
		this.parent = parent;
		this.depth = parent.getDepth() + 1;
	}

	public int getDepth() {

		return depth;
	}

	public boolean isRootNode() {
		return parent == null;
	}

	public Node getParent() {
		return parent;
	}

	public List<Node> getPathFromRoot() {
		Node current = this;
		AbstractQueue queue = new AbstractQueue();
		while (!(current.isRootNode())) {
			queue.addToFront(current);
			current = current.getParent();
		}
		queue.addToFront(current); // take care of root node
		return queue.asList();
	}

	public Object getState() {
		return state;
	}

	public void setAction(String action) {
		this.action = action;
		//actionStateMap.put("action", action);
	}

	public String getAction() {
		return action;
	}

	public void setStepCost(Double stepCost) {

		this.stepCost = stepCost;

	}

	public void addToPathCost(Double stepCost) {
		this.pathCost = new Double(parent.pathCost.doubleValue()
				+ stepCost.doubleValue());

	}

	/**
	 * @return Returns the pathCost.
	 */
	public double getPathCost() {

		return pathCost.doubleValue();
	}

	/**
	 * @return Returns the stepCost.
	 */
	public double getStepCost() {

		return stepCost.doubleValue();
	}
	
	public String toString(){
		return getState().toString();
	}
}