package aima.core.logic.planning.hierarchicalsearch;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;
import aima.core.logic.planning.State;

import java.util.*;

public class HierarchicalSearchAlgorithm {

    public List<ActionSchema> heirarchicalSearch(Problem problem) {
        Queue<List<ActionSchema>> frontier = new LinkedList<>();
        frontier.add(new ArrayList<>(Collections.singletonList(PlanningProblemFactory.getHlaAct(problem))));
        while (true) {
            System.out.println(frontier.size());
            if (frontier.isEmpty())
                return null;
            List<ActionSchema> plan = frontier.poll();
            int i = 0;
            ActionSchema hla;
            while (i < plan.size() && !(plan.get(i) instanceof HighLevelAction))
                i++;
            if (i < plan.size())
                hla = plan.get(i);
            else
                hla = null;
            if (hla != null)
                System.out.println("Hla ========" + hla.getName());
            else
                System.out.println("HLA is null");
            List<ActionSchema> prefix = new ArrayList<>();
            List<ActionSchema> suffix = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                prefix.add(plan.get(j));
            }
            System.out.println("Prefix ********");
            System.out.println(prefix.size());
            for (int j = i + 1; j < plan.size(); j++) {
                suffix.add(plan.get(j));
            }
            System.out.println("Suffix ********");
            System.out.println(suffix.size());
            State outcome = problem.getInitialState().result(prefix);
            System.out.println("State = **********");
            for (Literal literal :
                    outcome.getFluents()) {
                System.out.println(literal.toString());
            }
            if (hla == null) {
                if (outcome.getFluents().containsAll(problem.getGoalState().getFluents()))
                    return plan;
            } else {
                List<ActionSchema> tempInsertionList = new ArrayList<>();
                System.out.println("Refinements *******");
                System.out.println(refinements(hla, outcome).size());
                for (List<ActionSchema> sequence :
                        refinements(hla, outcome)) {
                    tempInsertionList.clear();
                    tempInsertionList.addAll(prefix);
                    tempInsertionList.addAll(sequence);
                    tempInsertionList.addAll(suffix);
                    frontier.add(tempInsertionList);
                }
            }
        }
    }

    private List<List<ActionSchema>> refinements(ActionSchema hla, State outcome) {
        List<List<ActionSchema>> result = new ArrayList<>();
        for (List<ActionSchema> refinement :
                ((HighLevelAction) hla).getRefinements()) {
            if (refinement.size()>0)
            if (outcome.isApplicable(refinement.get(0)))
                result.add(refinement);
            else
                result.add(refinement);
        }
        return result;
    }
}
