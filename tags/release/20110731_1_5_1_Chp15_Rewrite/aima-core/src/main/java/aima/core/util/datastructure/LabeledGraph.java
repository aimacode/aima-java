package aima.core.util.datastructure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Represents a directed labeled graph. Vertices are represented by their unique
 * labels and labeled edges by means of nested hashtables. Variant of class
 * {@code aima.util.Table}. This version is more dynamic, it requires no
 * initialization and can add new items whenever needed.
 * 
 * @author R. Lunde
 * @author Mike Stampone
 */
public class LabeledGraph<VertexLabelType, EdgeLabelType> {

	/**
	 * Lookup for edge label information. Contains an entry for every vertex
	 * label.
	 */
	private final Hashtable<VertexLabelType, Hashtable<VertexLabelType, EdgeLabelType>> globalEdgeLookup;
	/** List of the labels of all vertices within the graph. */
	private final List<VertexLabelType> vertexLabels;

	/** Creates a new empty graph. */
	public LabeledGraph() {
		globalEdgeLookup = new Hashtable<VertexLabelType, Hashtable<VertexLabelType, EdgeLabelType>>();
		vertexLabels = new ArrayList<VertexLabelType>();
	}

	/**
	 * Adds a new vertex to the graph if it is not already present.
	 * 
	 * @param v
	 *            the vertex to add
	 */
	public void addVertex(VertexLabelType v) {
		checkForNewVertex(v);
	}

	/**
	 * Adds a directed labeled edge to the graph. The end points of the edge are
	 * specified by vertex labels. New vertices are automatically identified and
	 * added to the graph.
	 * 
	 * @param from
	 *            the first vertex of the edge
	 * @param to
	 *            the second vertex of the edge
	 * @param el
	 *            an edge label
	 */
	public void set(VertexLabelType from, VertexLabelType to, EdgeLabelType el) {
		Hashtable<VertexLabelType, EdgeLabelType> localEdgeLookup = checkForNewVertex(from);
		localEdgeLookup.put(to, el);
		checkForNewVertex(to);
	}

	/** Handles new vertices. */
	private Hashtable<VertexLabelType, EdgeLabelType> checkForNewVertex(
			VertexLabelType v) {
		Hashtable<VertexLabelType, EdgeLabelType> result = globalEdgeLookup
				.get(v);
		if (result == null) {
			result = new Hashtable<VertexLabelType, EdgeLabelType>();
			globalEdgeLookup.put(v, result);
			vertexLabels.add(v);
		}
		return result;
	}

	/**
	 * Removes an edge from the graph.
	 * 
	 * @param from
	 *            the first vertex of the edge
	 * @param to
	 *            the second vertex of the edge
	 */
	public void remove(VertexLabelType from, VertexLabelType to) {
		Hashtable<VertexLabelType, EdgeLabelType> localEdgeLookup = globalEdgeLookup
				.get(from);
		if (localEdgeLookup != null)
			localEdgeLookup.remove(to);
	}

	/**
	 * Returns the label of the edge between the specified vertices, or null if
	 * there is no edge between them.
	 * 
	 * @param from
	 *            the first vertex of the ege
	 * @param to
	 *            the second vertex of the edge
	 * 
	 * @return the label of the edge between the specified vertices, or null if
	 *         there is no edge between them.
	 */
	public EdgeLabelType get(VertexLabelType from, VertexLabelType to) {
		Hashtable<VertexLabelType, EdgeLabelType> localEdgeLookup = globalEdgeLookup
				.get(from);
		return localEdgeLookup == null ? null : localEdgeLookup.get(to);
	}

	/**
	 * Returns the labels of those vertices which can be obtained by following
	 * the edges starting at the specified vertex.
	 */
	public List<VertexLabelType> getSuccessors(VertexLabelType v) {
		List<VertexLabelType> result = new ArrayList<VertexLabelType>();
		Hashtable<VertexLabelType, EdgeLabelType> localEdgeLookup = globalEdgeLookup
				.get(v);
		if (localEdgeLookup != null)
			result.addAll(localEdgeLookup.keySet());
		return result;
	}

	/** Returns the labels of all vertices within the graph. */
	public List<VertexLabelType> getVertexLabels() {
		return vertexLabels;
	}

	/** Checks whether the given label is the label of one of the vertices. */
	public boolean isVertexLabel(VertexLabelType v) {
		return globalEdgeLookup.get(v) != null;
	}

	/** Removes all vertices and all edges from the graph. */
	public void clear() {
		vertexLabels.clear();
		globalEdgeLookup.clear();
	}
}
