package aima.core.logic.planning.angelicsearch;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.Problem;
import aima.core.logic.planning.State;

import java.util.*;

public class AngelicSearchAlgorithm {
    public List<Object> angelicSearch(Problem problem, List<Object> initialPlan) {
        Queue<List<Object>> frontier = new LinkedList<>();
        frontier.add(initialPlan);
        while (true) {
            if (frontier.isEmpty())
                return null;
            List<Object> plan = frontier.poll();

            boolean flag = false;
            for (State state :
                    problem.getInitialState().optimisticReach(plan)) {
                if (state.getFluents().containsAll(problem.getGoalState().getFluents()))
                    flag = true;
            }
            if (flag) {
                if (checkPrimitive(plan))
                    return plan;
                HashSet<State> guaranteed = problem.getInitialState().pessimisticReach(plan);
                guaranteed.add(problem.getGoalState());
                if ((!guaranteed.isEmpty()) && makingProgress(plan, initialPlan)) {
                    State finalState = guaranteed.iterator().next();
                    return decompose(problem.getInitialState(), plan, finalState);
                }
                int i = 0;
                AngelicHLA hla;
                while (i < plan.size() && !(plan.get(i) instanceof AngelicHLA))
                    i++;
                if (i < plan.size())
                    hla = (AngelicHLA) plan.get(i);
                else
                    hla = null;
                // prefix,suffix ← the action subsequences before and after hla in plan
                List<Object> prefix = new ArrayList<>();
                List<Object> suffix = new ArrayList<>();
                for (int j = 0; j < i; j++) {
                    prefix.add( plan.get(j));
                }
                for (int j = i + 1; j < plan.size(); j++) {
                    suffix.add(plan.get(j));
                }
                List<Object> tempInsertionList = new ArrayList<>();
                HashSet<State> outcomeStates = problem.getInitialState().optimisticReach(prefix);
                for (List<Object> sequence :
                     refinements(hla,outcomeStates)){
                    tempInsertionList.clear();
                    tempInsertionList.addAll(prefix);
                    tempInsertionList.addAll(sequence);
                    tempInsertionList.addAll(suffix);
                    ((LinkedList<List<Object>>) frontier).addLast(new ArrayList<>(tempInsertionList));
                }
            }
            return plan;
        }
    }

    private boolean makingProgress(List<Object> plan, List<Object> initialPlan) {
        return !(plan.containsAll(initialPlan)&&initialPlan.containsAll(plan));
    }

    private boolean checkPrimitive(List<Object> plan) {
        for (Object obj :
                plan) {
            if (!(obj instanceof ActionSchema))
                return false;
        }
        return true;
    }

    private List<Object> decompose(State initialState, List<Object> plan, State finalState) {
        List<Object> solution = new ArrayList<>();
        while(!plan.isEmpty()){
            Object action = plan.remove(plan.size()-1);
            State si = new State("");
            for (State state :
                    initialState.pessimisticReach(plan)) {
                if (state.pessimisticReach(Collections.singletonList(action)).contains(finalState)){
                    si = state;
                    break;
                }
            }
            Problem problem = new Problem(si,finalState,new HashSet<>());
            solution.add(angelicSearch(problem,Collections.singletonList(action)));
            finalState = si;
        }
        return solution;
    }

    public List<List<Object>> refinements(AngelicHLA hla, HashSet<State> outcome) {
        List<List<Object>> result = new ArrayList<>();
        for (List<Object> refinement :
                 hla.getRefinements()) {
            if (refinement.size() > 0) {
                if (refinement.get(0) instanceof AngelicHLA) {
                    for (State state :
                            outcome) {
                        if (state.isApplicable((AngelicHLA) refinement.get(0))) {
                            result.add(refinement);
                            break;
                        }
                    }
                } else {
                    for (State state :
                            outcome) {
                        if (state.isApplicable((ActionSchema) refinement.get(0))) {
                            result.add(refinement);
                            break;
                        }
                    }
                }
            } else
                result.add(refinement);
        }
        return result;
    }
}

