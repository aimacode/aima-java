package aima.core.search.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides several useful static methods for implementing search.
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class SearchUtils {
	/**
	 * Returns the list of actions corresponding to the complete path to the
	 * given node or a list with just one null reference representing NoOp
	 * if the node is the root node of the search tree.
	 */
	public static <S, A> List<A> getSequenceOfActions(Node<S, A> node) {
		List<Node<S, A>> nodes = node.getPathFromRoot();
		List<A> actions = new ArrayList<>();
		if (nodes.size() == 1) {
			// I'm at the root node, this indicates I started at the
			// Goal node, therefore just return a NoOp
			actions.add(null);
		} else {
			// ignore the root node this has no action
			// hence index starts from 1 not zero
			for (int i = 1; i < nodes.size(); i++)
				actions.add(nodes.get(i).getAction());
		}
		return actions;
	}

	/** Returns an empty action list. */
	public static <A> List<A> failure() {
		return Collections.emptyList();
	}
	
	/** Checks whether a list of actions is empty. */
	public static <A> boolean isFailure(List<A> actions) {
		return actions.isEmpty();
	}
}