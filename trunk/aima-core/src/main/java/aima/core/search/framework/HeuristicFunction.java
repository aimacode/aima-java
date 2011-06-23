package aima.core.search.framework;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * a heuristic function, denoted h(n):<br>
 * h(n) = estimated cost of the cheapest path from the state at node n to a goal
 * state.<br>
 * <br>
 * Notice that h(n) takes a node as input, but, unlike g(n) it depends only on
 * the state at that node.
 * 
 * @author Ravi Mohan
 * 
 */
public interface HeuristicFunction {
	double h(Object state);
}