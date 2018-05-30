package aima.core.logic.planning.angelicsearch;

import aima.core.logic.planning.Problem;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AngelicSearchAlgorithm {
    public List<Object> angelicSearch(Problem problem, List<Object> initialPlan){
        Queue<List<Object>> frontier = new LinkedList<>();
        frontier.add(initialPlan);
        while(true) {
            if (frontier.isEmpty())
                return null;
            List<Object> plan = frontier.poll();
            // if (problem.getInitialState().optimisticReach(plan)){
            //   if(plan is primitive)
            return plan;

        }
    }
}

