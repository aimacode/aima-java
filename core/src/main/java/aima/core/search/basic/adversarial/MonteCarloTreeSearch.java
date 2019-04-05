package aima.core.search.basic.adversarial;

import aima.core.search.api.Game;
import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchForAdversarialActionFunction;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.GameTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 *
 * <pre>
 * <code>
 * function MONTE-CARLO-TREE-SEARCH(state) returns an action
 *   tree &larr; NODE(state)
 *   while TIME-REMAINING() do
 *   	leaf &larr; SELECT(tree)
 *   	child &larr; EXPAND(leaf)
 *   	result &larr; SIMULATE(child)
 *   	BACKPROPAGATE(result, child)
 *   return the move in ACTIONS(state) whose node has highest number of playouts
 * </code>
 * </pre>
 *
 * Figure ?.? The Monte Carlo tree search algorithm. A game tree, tree, is initialized, and
 * then we repeat the cycle of SELECT / EXPAND / SIMULATE/ BACKPROPAGATE until we run  out
 * of time, and return the move that led to the node with the highest number of playouts.
 *
 *
 * @author Suyash Jain
 *
 * @param <S>
 *            Type which is used for states in the game.
 * @param <A>
 *            Type which is used for actions in the game.
 * @param <P>
 *            Type which is used for players in the game.
 */

public class MonteCarloTreeSearch<S, A, P> implements SearchForAdversarialActionFunction<S, A> {
	protected Game<S, A, P> game;
	protected GameTree<A, S> tree;
	
	public MonteCarloTreeSearch(Game<S, A, P> game) {
		this.game = game;
		tree = new GameTree();
	}
	
	// function MONTE-CARLO-TREE-SEARCH(state) returns an action
	private A monteCarloTreeSearch(S state) {
		
		// tree <-- NODE(state)
		tree.addRoot(state);
		// while TIME-REMAINING() do
		while () {
			// leaf <-- SELECT(tree)
			Node<A, S> leaf = select(tree);
			// child <-- EXPAND(leaf)
			Node<A, S> child = expand(leaf);
			// result <-- SIMULATE(child)
			// result = true if player of root node wins
			boolean result = simulate(child);
			// BACKPROPAGATE(result, child)
			backpropagate(result, child);
		}
		// return the move in ACTIONS(state) whose node has highest number of playouts
		return bestAction(tree.getRoot());
	}
	
	private Node<A, S> select(GameTree gameTree) {
		Node node = gameTree.getRoot();
		while (isNodeFullyExpanded(node)) {
			node = gameTree.getChildWithMaxUCT(node);
		}
		return node;
	}
	
	private Node<A, S> expand(Node<A, S> leaf) {
		if (game.isTerminalState(leaf.state())) return leaf;
		else {
			Node<A, S> child = randomlySelectUnvisitedChild(leaf);
			return child;
		}
	}
	
	private boolean simulate(Node<A, S> node) {
		while (!game.isTerminalState(node.state())) {
			Random rand = new Random();
			A a = game.actions(node.state()).get(rand.nextInt(game.actions(node.state()).size()));
			S result = game.result(node.state(), a);
			NodeFactory nodeFactory = new BasicNodeFactory();
			node = nodeFactory.newRootNode(result);
		}
		if (game.utility(node.state(), game.player(tree.getRoot().state())) > 0) return true;
		else return false;
	}
	
	private void backpropagate(boolean result, Node<A, S> node) {
		tree.updateStats(result, node);
		if (tree.getParent(node) != null) backpropagate(result, tree.getParent(node));
	}
	
	private A bestAction(Node<A, S> root) {
		Node<A, S> bestChild = tree.getChildWithMaxUCT(root);
		for (A a : game.actions(root.state())) {
			S result = game.result(root.state(), a);
			if (result == bestChild.state()) return a;
		}
		return null;
	}
	
	private boolean isNodeFullyExpanded(Node<A, S> node) {
		for (A a : game.actions(node.state())) {
			S result = game.result(node.state(), a);
			if (!tree.contains(result)) {
				return false;
			}
		}
		return true;
	}
	
	
	private Node<A, S> randomlySelectUnvisitedChild(Node<A, S> node) {
		List<S> unvisitedChildren = new ArrayList<>();
		for (A a : game.actions(node.state())) {
			S result = game.result(node.state(), a);
			if (!tree.contains(result)) unvisitedChildren.add(result);
		}
		Random rand = new Random();
		Node<A, S> newChild = tree.addChild(node, unvisitedChildren.get(rand.nextInt(unvisitedChildren.size())));
		return newChild;
	}
	
	@Override
	public A apply(S state) {
		return monteCarloTreeSearch(state);
	}
	
	
}
