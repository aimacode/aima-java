package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;

/**
 * An interface that identifies a generic search algorithm.
 *
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function GENERIC-SEARCH(problem) returns a solution, or failure
 *  frontier ← a queue initially containing one path, for the problem's initial state
 *  reached ← a table of {state: the best path that reached state}; initially empty
 *  solution ← failure
 *  while frontier is not empty and solution can possibly be improved do
 *    parent ← some node that we choose to remove from frontier
 *    for child in successors(parent) do
 *      s ← child.state
 *      if s is not in reached or child is a cheaper path than reached[s] then
 *        reached[s] ← child
 *        add child to frontier
 *        if child is a goal and is cheaper than solution then
 *          solution = child
 *  return solution
 * </pre>
 * <p>
 * Figure ?? In the GENERIC-SEARCH algorithm, we keep track of the best
 * solution found so far, as well as a set of states that we have already
 * reached, and a frontier of paths from which we will choose the next path
 * to expand. In any specific search algorithm, we specify (1) the criteria
 * for ordering the paths in the frontier, and (2) the procedure for
 * determining when it is no longer possible to improve on a solution.
 *
 * @author samagra
 */
public interface GenericSearchInterface<A,S> {
    Node<A,S> search(Problem<A,S> problem);
}
