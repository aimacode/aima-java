package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.Hashtable;
import java.util.List;

public class GraphPlanAlgorithm {
    boolean graphPlan(Problem problem){
        Graph graph = initialPlanningGraph(problem);
        List<Literal> goals = conjuncts(problem.goalState);
        Hashtable<Object,Object> nogoods = new Hashtable<>();
        Level state;
        for(int tl = 0;;tl++){
            state = graph.levels[2*tl];
            boolean mutexCheck = true;
            if(state.levelObjects.containsAll(goals)){
                mutexCheck = false;
                for (Object literal :
                        goals) {
                    List<Object> mutexOfGoal = state.mutexLinks.get(literal);
                    for (Object object:
                         mutexOfGoal) {
                        if (goals.contains(object)){
                            mutexCheck = true;
                            break;
                        }
                    }
                }
            }
            if(!mutexCheck){
                boolean solution = extractSolution(graph,goals,graph.numLevels(),nogoods);
                if(solution)
                    return solution;
            }
            if(levelledOff(graph)&&leveledOff(nogoods))
                return false;
            graph = expandGraph(graph,problem);
        }
    }

    private List<Literal> conjuncts(State goalState) {
        return goalState.getFluents();
    }

    private Graph initialPlanningGraph(Problem problem) {
        Level initialLevel = new Level();
    }
}
