/**
 * This package (together with its subpackages) contains base classes for search
 * algorithm implementations. Common interfaces are defined by
 * {@link aima.core.search.framework.SearchForActions} and
 * {@link aima.core.search.framework.SearchForStates}. Most of the concrete
 * algorithms implement both of them. Many search algorithms are basically
 * queue-based algorithms. They construct a tree of nodes which represents the
 * possible sequences of actions and the corresponding resulting states. A queue
 * is used to manage and prioritize the current end points of already analyzed
 * sequences.
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
 * search strategies) are provided as specializations of
 * {@link aima.core.search.framework.PrioritySearch}. They delegate the work of
 * controlling the actual search to some
 * {@link aima.core.search.framework.qsearch.QueueSearch} implementation. The
 * most important concrete implementations are TreeSearch, GraphSearch, and
 * BidirectionalSearch.
 * 
 * <br>
 * Here, all search strategies explore the search space by expanding nodes. A
 * central {@link aima.core.search.framework.NodeExpander} class is used for
 * this purpose. The default implementation should work for most purposes, but
 * it is possible to equip search algorithms with specialized versions (e.g.
 * which modify path cost computation - extra costs for move direction changes).
 * The node structure is needed when searching for sequences of actions (just
 * follow parent links after a goal state node was found). Defining search for
 * states (e.g. in a local search strategy) based on nodes makes sense, too.
 * Nodes do not necessary increase space complexity as long as parent links can
 * be switched off. However, by switching on parent links, those algorithms can
 * be turned into search for actions algorithms. Additionally, the common node
 * expander interface unifies progress tracing for all search algorithms (just
 * add a node listener to get notifications about expanded nodes).
 * 
 * @author Ruediger Lunde
 */

package aima.core.search.framework;
