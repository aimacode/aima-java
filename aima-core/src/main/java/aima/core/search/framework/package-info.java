/**
 * This package contains base classes for search algorithm implementations. Many
 * algorithms are basically queue search algorithms. They construct a tree of
 * nodes which represents the possible sequences of actions and the
 * corresponding resulting states. A queue is used to manage and prioritize
 * the current end points of already analyzed sequences of actions.
 * <br>
 * Specializations are possible in two ways: Prioritization can be done by
 * time (e.g. first come first serve), by comparator or by evaluation function.
 * All this different strategies can be combined with different loop/cycle handling
 * strategies to avoid incompleteness and improve efficiency.
 * <br>
 * Here, the bridge pattern is used. Different abstractions of search are provided
 * as subclasses of <code>Search</code> and especially <code>PrioritySearch</code>.
 * Most of them delegate the work to some <code>QueueSearch</code> implementation.
 * The two important concrete implementations are <code>TreeSearch</code> and
 * <code>GraphSearch</code>. 
 * 
 * @author Ruediger Lunde
 */

package aima.core.search.framework;
