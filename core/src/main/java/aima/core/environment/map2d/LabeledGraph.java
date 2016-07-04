package aima.core.environment.map2d;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a directed labeled graph. Vertices are represented by their unique
 * labels and labeled edges by means of nested maps.
 *
 * @param <V>
 *            the type of the vertex.
 * @param <E>
 *            the type of the edge.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class LabeledGraph<V, E> {

	// Lookup for edge label information. Contains an entry for every vertex label.
	private final Map<V, Map<V, E>> globalEdgeLookup;
	// List of the labels of all vertices within the graph.
	private final List<V> vertexLabels;

	/**
	 * Default constructor. Creates a new empty graph.
	 */
	public LabeledGraph() {
		globalEdgeLookup = new LinkedHashMap<>();
		vertexLabels = new ArrayList<>();
	}

	/**
	 * Adds a new vertex to the graph if it is not already present.
	 *
	 * @param v
	 *            the vertex to add
	 */
	public void addVertex(V v) {
		checkForNewVertex(v);
	}

	/**
	 * Adds a directed labeled edge to the graph. The end points of the edge are
	 * specified by vertex labels. New vertices are automatically identified and
	 * added to the graph.
	 *
	 * @param from
	 *            the from vertex of the edge
	 * @param to
	 *            the to vertex of the edge
	 * @param edge
	 *            an edge
	 */
	public void set(V from, V to, E edge) {
		Map<V, E> localEdgeLookup = checkForNewVertex(from);
		localEdgeLookup.put(to, edge);
		checkForNewVertex(to);
	}

	/**
	 * Removes an edge from the graph.
	 *
	 * @param from
	 *            the from vertex of the edge
	 * @param to
	 *            the to vertex of the edge
	 */
	public void remove(V from, V to) {
		Map<V, E> localEdgeLookup = globalEdgeLookup.get(from);
		if (localEdgeLookup != null) {
			localEdgeLookup.remove(to);
		}
	}

	/**
	 * Get the label of the edge between the specified vertices, or null if
	 * there is no edge between them.
	 *
	 * @param from
	 *            the from vertex of the ege
	 * @param to
	 *            the to vertex of the edge
	 *
	 * @return the label of the edge between the specified vertices, or null if
	 *         there is no edge between them.
	 */
	public E get(V from, V to) {
		Map<V, E> localEdgeLookup = globalEdgeLookup.get(from);
		return localEdgeLookup == null ? null : localEdgeLookup.get(to);
	}

	/**
	 * Get the immediate successor vertices which can be reached by following
	 * the edges starting at the specified vertex.
	 *
	 * @param v
	 *            the starting vertex
	 * @return the immediate successor vertices which can be reached by
	 *         following the edges starting at the specified vertex.
	 */
	public List<V> getSuccessors(V v) {
		List<V> result = new ArrayList<>();
		Map<V, E> localEdgeLookup = globalEdgeLookup.get(v);
		if (localEdgeLookup != null) {
			result.addAll(localEdgeLookup.keySet());
		}
		return result;
	}

	/**
	 *
	 * @return the labels of all vertices within the graph.
	 */
	public List<V> getVertexLabels() {
		return vertexLabels;
	}

	/**
	 * Checks whether the given vertex label is contained within the graph.
	 *
	 * @param v
	 *            the vertex to check.
	 * @return true if the given vertex label is contained within the graph,
	 *         false otherwise.
	 */
	public boolean isVertexLabel(V v) {
		return globalEdgeLookup.get(v) != null;
	}

	/**
	 * Remove all vertices and edges from the graph.
	 */
	public void clear() {
		vertexLabels.clear();
		globalEdgeLookup.clear();
	}

	//
	// PRIVATE METHODS
	//

	// Handles new vertices
	private Map<V, E> checkForNewVertex(V v) {
		Map<V, E> result = globalEdgeLookup.get(v);
		if (result == null) {
			result = new LinkedHashMap<>();
			globalEdgeLookup.put(v, result);
			vertexLabels.add(v);
		}
		return result;
	}
}
