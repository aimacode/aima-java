/**
 * This package (together with its subpackages) contains base
 * classes for search algorithm implementations. Many search algorithms are
 * basically queue-based algorithms. They construct a tree of nodes which
 * represents the possible sequences of actions and the corresponding resulting
 * states. A queue is used to manage and prioritize the current end points of
 * already analyzed sequences.
 * 
 * <br>
 * Specializations are possible in two ways: There are different ways to define
 * a queue (A), and to use the queue to explore the search space (B). (A) is
 * about prioritizing nodes, which can be done by time (e.g. first come first
 * serve), by comparator, or by evaluation function. (B) is about controlling
 * the simulated exploration of the search space based on a given queue data
 * structure. This includes strategies for filtering nodes to avoid getting
 * stuck in loops.
 * 
 * <br>
 * To support arbitrary combinations of different strategies for (A) and (B),
 * the bridge pattern is used here. Different abstractions of search (so called
 * search strategies) are provided as specializations of {@link Search} and
 * especially {@link PrioritySearch}. Most of them delegate the work to some
 * {@link aima.core.search.framework.qsearch.QueueSearch} implementation. The
 * most important concrete implementations are TreeSearch, GraphSearch, and
 * BidirectionalSearch.
 * 
 * @author Ruediger Lunde
 */

package aima.core.search.framework;
