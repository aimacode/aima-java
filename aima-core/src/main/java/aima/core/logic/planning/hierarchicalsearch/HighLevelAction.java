package aima.core.logic.planning.hierarchicalsearch;

import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.planning.ActionSchema;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 11.4, page
 * 409.<br>
 * <p>
 * Each HLA has one or more possible refinements, into a sequence 1
 * of actions, each of which may be an HLA or a primitive action (which has no refinements
 * by definition).
 *
 * @author samagra
 * @author Ruediger Lunde
 */
public class HighLevelAction extends ActionSchema {
    List<List<ActionSchema>> refinements;

    public HighLevelAction(String name, List<Term> variables, String precondition, String effects,
                           List<List<ActionSchema>> refinements) {
        super(name, variables, precondition, effects);
        this.refinements = refinements;
    }

    public void addRefinement(List<ActionSchema> newRefinement) {
        refinements.add(newRefinement);
    }

    public List<List<ActionSchema>> getRefinements() {
        return refinements;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append("\nREFINEMENTS:");
        for (List<ActionSchema> refinement : getRefinements()) {
            result.append("\n\t");
            String sep = "";
            for (ActionSchema action : refinement) {
                result.append(sep).append(action.getName());
                sep = ", ";
            }
        }
        return result.toString();
    }
}
