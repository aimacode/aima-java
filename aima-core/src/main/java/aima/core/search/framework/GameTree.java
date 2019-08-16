package aima.core.search.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *	Basic implementation of Game Tree for the Monte Carlo Tree Search
 *
 * 	Wi stands for the number of wins for the node considered after the i-th move.
 * 	Ni stands for the number of simulations for the node considered after the i-th move.
 *
 * @author Suyash Jain
 */

public class GameTree<S, A> {
	HashMap<Node<S, A>, List<Node<S, A>>> gameTree;
	HashMap<S, Double> Wi, Ni;
	NodeFactory<S, A> nodeFactory;
	Node<S, A> root;
	
	
	public GameTree() {
		this.gameTree = new HashMap<>();
		this.nodeFactory = new NodeFactory<>();
		Wi = new HashMap<>();
		Ni = new HashMap<>();
	}
	
	public void addRoot(S root) {
		Node<S, A> rootNode = nodeFactory.createNode(root);
		this.root = rootNode;
		gameTree.put(rootNode, new ArrayList<>());
		Wi.put(root, 0.0);
		Ni.put(root, 0.0);
	}
	
	public Node<S, A> getRoot() {
		return root;
	}
	
	public List<S> getVisitedChildren(Node<S, A> parent) {
		List<S> visitedChildren = new ArrayList<>();
		if (gameTree.containsKey(parent)) {
			for (Node<S, A> child : gameTree.get(parent)) {
				visitedChildren.add(child.getState());
			}
		}
		return visitedChildren;
	}
	
	public Node<S, A> addChild(Node<S, A> parent, S child) {
		Node<S, A> newChild = nodeFactory.createNode(child);
		List<Node<S, A>> children = successors(parent);
		children.add(newChild);
		gameTree.put(parent, children);
		Wi.put(child, 0.0);
		Ni.put(child, 0.0);
		return newChild;
	}
	
	public Node<S, A> getParent(Node<S, A> node) {
		Node<S, A> parent = null;
		for (Node<S, A> key : gameTree.keySet()) {
			List<Node<S, A>> children = successors(key);
			for (Node<S, A> child : children) {
				if (child.getState() == node.getState()) {
					parent = key;
					break;
				}
			}
			if (parent != null) break;
		}
		return parent;
	}
	
	public List<Node<S, A>> successors(Node<S, A> node) {
		if (gameTree.containsKey(node)) return gameTree.get(node);
		else return new ArrayList<>();
	}
	
	public void updateStats(boolean result, Node<S, A> node) {
		Ni.put(node.getState(), Ni.get(node.getState()) + 1);
		if (result) Wi.put(node.getState(), Wi.get(node.getState()) + 1);
	}
	
	public Node<S, A> getChildWithMaxUCT(Node<S, A> node) {
		List<Node<S, A>> best_children = new ArrayList<>();
		double max_uct = Double.NEGATIVE_INFINITY;
		for (Node<S, A> child : successors(node)) {
			double uct = ((Wi.get(child.getState())) / (Ni.get(child.getState()))) + Math.sqrt((2 / Ni.get(child.getState())) * (Math.log(Ni.get(node.getState()))));
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
	
	public Node<S, A> getChildWithMaxPlayouts(Node<S, A> node) {
		List<Node<S, A>> best_children = new ArrayList<>();
		double max_playouts = Double.NEGATIVE_INFINITY;
		for (Node<S, A> child : successors(node)) {
			double playouts = (Ni.get(child.getState()));
			if (playouts > max_playouts) {
				max_playouts = playouts;
				best_children = new ArrayList<>();
				best_children.add(child);
			} else if (playouts == max_playouts) {
				best_children.add(child);
			}
		}
		Random rand = new Random();
		return best_children.get(rand.nextInt(best_children.size()));
	}
}
