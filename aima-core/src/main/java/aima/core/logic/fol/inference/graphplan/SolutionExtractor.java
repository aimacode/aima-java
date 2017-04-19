package aima.core.logic.fol.inference.graphplan;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.util.datastructure.LabeledGraph;
import aima.core.util.datastructure.Pair;

import java.util.HashSet;
import java.util.List;

/**
 * Interface to be implemented to extract solution from a planning graph
 * @author Matt Grenander
 */
public interface SolutionExtractor {
    List<PDDLAction> extractSolution(LabeledGraph<GraphPlanNode, MutexLink> graph, List<Literal> goals, int numLevels, HashSet<Pair<Integer, List<Literal>>> nogoods);
}
