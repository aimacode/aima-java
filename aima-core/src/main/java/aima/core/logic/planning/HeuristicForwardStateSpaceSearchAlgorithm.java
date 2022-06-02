package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.search.framework.Node;
import aima.core.search.framework.QueueBasedSearch;
import aima.core.search.framework.problem.StepCostFunction;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.informed.AStarSearch;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * Implements a simple heuristic forward state-space search algorithm using A*. By default, a level sum heuristic
 * is used. Heuristic function as well as the step-cost function can be replaced.
 *
 * @author Ruediger Lunde
 */
public class HeuristicForwardStateSpaceSearchAlgorithm {

    private ToDoubleFunction<Node<List<Literal>, ActionSchema>> heuristicFn = n -> levelSumHeuristic(n.getState());
    private StepCostFunction<List<Literal>, ActionSchema> stepCostFn = (s0, a, s1) -> 1;

    private final PlanningProblem pProblem;
    private final Graph graph;
    private QueueBasedSearch<List<Literal>, ActionSchema> search;
    private final HashMap<Literal, Integer> literalLookup = new HashMap<>();

    public HeuristicForwardStateSpaceSearchAlgorithm(final PlanningProblem pProblem) {
        this.pProblem = pProblem;
        // Compute planning graph. It is used to filter propositionalised actions and for heuristic value computation.
        graph = new Graph(pProblem);
        while (!graph.levelledOff())
            graph.expand(pProblem);
        literalLookup.clear();
    }

    public void setHeuristicFn(ToDoubleFunction<Node<List<Literal>, ActionSchema>> heuristicFn) {
        this.heuristicFn = heuristicFn;
    }

    public void setStepCostFn(StepCostFunction<List<Literal>, ActionSchema> stepCostFn) {
        this.stepCostFn = stepCostFn;
    }

    public Optional<List<ActionSchema>> search() {
        ForwardStateSpaceSearchProblem sProblem =
                new ForwardStateSpaceSearchProblem(pProblem, new ArrayList<>(graph.getAllActions()), stepCostFn);
        search = new AStarSearch<>(new GraphSearch<>(), heuristicFn);
        return search.findActions(sProblem);
    }

    public QueueBasedSearch<List<Literal>, ActionSchema> getSearch() {
        return search;
    }

    private double levelSumHeuristic(List<Literal> state) {
        double result = 0;
        for (Literal literal : pProblem.getGoal()) {
            if (!state.contains(literal)) {
                Integer level = literalLookup.get(literal);
                if (level == null) {
                    level = graph.getFirstLevelOfOccurrence(literal);
                    literalLookup.put(literal, level);
                }
                result = result + level;
            }
        }
        return result;
    }
}
