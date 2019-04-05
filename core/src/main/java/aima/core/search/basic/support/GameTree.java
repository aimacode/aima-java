package aima.core.search.basic.support;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *	Basic implementation of Game Tree for the Monte Carlo Search
 *
 * 	Wi stands for the number of wins for the node considered after the i-th move.
 * 	Ni stands for the number of simulations for the node considered after the i-th move.
 *
 * @author Suyash Jain
 */

public class GameTree<A, S>{
	
	HashMap<Node<A, S>, List<Node<A, S>>> gameTree;
	HashMap<S, Double> Wi, Ni;
	NodeFactory<A, S> nodeFactory;
	Node<A, S> root;
	
	
	public GameTree() {
		this.gameTree = new HashMap<>();
		this.nodeFactory = new BasicNodeFactory<>();
		Wi = new HashMap<>();
		Ni = new HashMap<>();
	}
	
	public void addRoot(S root) {
		Node<A, S> rootNode = nodeFactory.newRootNode(root);
		this.root = rootNode;
		gameTree.put(rootNode, new ArrayList<>());
		Wi.put(root, 0.0);
		Ni.put(root, 0.0);
	}
	
	public Node<A, S> getRoot(){
		return root;
	}
	
	public Node<A, S> addChild(Node<A, S> parent, S child) {
		Node<A, S> newChild = nodeFactory.newRootNode(child);
		List<Node<A, S>> children = successors(parent);
		children.add(newChild);
		gameTree.put(parent, children);
		Wi.put(newChild.state(), 0.0);
		Ni.put(newChild.state(), 0.0);
		return newChild;
	}
	
	public Node<A, S> getParent(Node<A, S> node) {
		Node<A, S> parent = null;
		for (Node<A, S> key : gameTree.keySet()) {
			List<Node<A, S>> children = successors(key);
			for (Node<A, S> child : children) {
				if (child.state() == node.state()) {
					parent = key;
					break;
				}
			}
			if (parent != null) break;
		}
		return parent;
	}
	
	public List<Node<A, S>> successors(Node<A, S> node) {
		return gameTree.get(node);
	}
	
	public boolean contains(S state) {
		return Ni.containsKey(state);
	}
	
	public void updateStats(boolean result, Node<A, S> node){
		Ni.put(node.state(), Ni.get(node.state()) + 1);
		if (result) Wi.put(node.state(), Wi.get(node.state()) + 1);
	}
	
	public Node<A, S> getChildWithMaxUCT(Node<A, S> node) {
		List<Node<A, S>> best_children = new ArrayList<>();
		double max_uct = Double.NEGATIVE_INFINITY;
		for (Node<A, S> child : successors(node)) {
			double uct = ((Wi.get(child.state())) / (Ni.get(child.state()))) + Math.sqrt((2 / Ni.get(child.state())) * (Math.log(Ni.get(node.state()))));
			if (uct > max_uct) {
				max_uct = uct;
				best_children = new ArrayList<>();
				best_children.add(child);
			} else if (uct == max_uct) {
				best_children.add(child);
			}
		}
		Random rand = new Random();
		return best_children.get(rand.nextInt(best_children.size()));
	}
}
