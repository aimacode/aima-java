package aima.core.logic.planning.hierarchicalsearch;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;

import java.util.*;

public class HierarchicalSearchAlgorithm {

    public boolean heirarchicalSearch(Problem problem){
        Queue<List<HighLevelAction>> frontier = new LinkedList<>();
        frontier.add(new ArrayList<>(Collections.singletonList(PlanningProblemFactory.getHlaAct(problem))));
        while(true){
            if (frontier.isEmpty())
                return false;
            List<HighLevelAction> plan = frontier.poll();
            HighLevelAction hla = plan.get(0);
        }
    }
}
