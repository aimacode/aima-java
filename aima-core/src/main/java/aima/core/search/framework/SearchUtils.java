package aima.core.search.framework;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Provides several useful static methods for implementing search.
 *
 * @author Ruediger Lunde
 */
public class SearchUtils {

	/**
	 * Returns the path from the root node to this node.
	 *
	 * @return the path from the root node to this node.
	 */
	public static <S, A> List<Node<S, A>> getPathFromRoot(Node<S, A> node) {
		List<Node<S, A>> path = new LinkedList<>();
		while (!node.isRootNode()) {
			path.add(0, node);
			node = node.getParent();
		}
		// ensure the root node is added
		path.add(0, node);
		return path;
	}

	/**
	 * Returns the list of actions which corresponds to the complete path to the
	 * given node. The list is empty, if the node is the root node of the search
	 * tree.
	 */
	public static <S, A> List<A> getSequenceOfActions(Node<S, A> node) {
		List<A> actions = new LinkedList<>();
		while (!node.isRootNode()) {
			actions.add(0, node.getAction());
			node = node.getParent();
		}
		return actions;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static <S, A> Optional<List<A>> toActions(Optional<Node<S, A>> node) {
		return node.isPresent() ? Optional.of(getSequenceOfActions(node.get())) : Optional.empty();
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static <S, A> Optional<S> toState(Optional<Node<S, A>> node) {
		return node.isPresent() ? Optional.of(node.get().getState()) : Optional.empty();
	}
}