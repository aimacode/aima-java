package aima.core.search.csp;

import java.util.*;

import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.11, Page
 * 224.<br>
 * <br>
 * <p>
 * <pre>
 * <code>
 * function TREE-CSP-SOLVER(csp) returns a solution, or failure
 * 		inputs: csp, a CSP with components X, D, C
 * 		n &larr; number of variables in X
 * 		assignment &larr; an empty assignment
 * 		root  &larr; any variable in X
 * 		X &larr; TOPOLOGICALSORT(X, root )
 * 		for j = n down to 2 do
 * 			MAKE-ARC-CONSISTENT(PARENT(Xj),Xj )
 * 			if it cannot be made consistent then return failure
 * 		for i = 1 to n do
 * 			assignment[Xi] &larr; any consistent value from Di
 * 			if there is no consistent value then return failure (*)
 * 		return assignment
 * </code>
 *
 * <pre>
 *
 * Figure 6.11 The TREE-CSP-SOLVER algorithm for solving tree-structured CSPs.
 * If the CSP has a solution, we will find it in linear time; if not, we will
 * detect a contradiction. Comment to (*) (RL): If no empty domain was found in the
 * previous loop, this will never happen.
 *
 * @author Ruediger Lunde
 * @author Anurag Rai
 */
public class TreeCspSolver<VAR extends Variable, VAL> extends CspSolver<VAR, VAL> {

    @Override
    public Assignment<VAR, VAL> solve(CSP<VAR, VAL> csp) {

        Assignment<VAR, VAL> assignment = new Assignment<>();
        // Select a random root from the List of Variables
        VAR root = Util.selectRandomlyFromList(csp.getVariables());
        // Sort the variables in topological order
        List<VAR> orderedVars = new ArrayList<>();
        Map<VAR, Constraint<VAR, VAL>> parentConstraints = new HashMap<>();
        topologicalSort(csp, root, orderedVars, parentConstraints);
        if (orderedVars.size() < csp.getVariables().size())
            return null; // CSP is not tree-structured or not connected or has no solution!

        // Establish arc consistency from top to bottom (starting at the bottom).
        csp = csp.copyDomains(); // do not change the original CSP!
        for (int i = orderedVars.size() - 1; i >= 1; i--) {
            VAR var = orderedVars.get(i);
            Constraint<VAR, VAL> constraint = parentConstraints.get(var);
            VAR parent = csp.getNeighbor(var, constraint);
            if (makeArcConsistent(parent, var, constraint, csp)) {
                if (csp.getDomain(parent).isEmpty())
                    return null; // CSP has no solution!
            }
        }

        // Assign values to variables from top to bottom.
        for (int i = 0; i < orderedVars.size(); i++) {
            VAR var = orderedVars.get(i);
            for (VAL value : csp.getDomain(var)) {
                assignment.add(var, value);
                if (assignment.isConsistent(csp.getConstraints(var)))
                    break;
            }
        }
        return assignment;
    }

    /**
     * Computes an explicit representation of the tree structure and a total order which is consistent with the
     * parent-child relations.
     *
     * @param csp               A CSP
     * @param root              A root variable
     * @param orderedVars       The computed total order (initially empty)
     * @param parentConstraints The tree structure, maps a variable to the constraint representing the arc to the parent
     *                          variable (initially empty)
     */
    private void topologicalSort(CSP<VAR, VAL> csp, VAR root, List<VAR> orderedVars,
                                 Map<VAR, Constraint<VAR, VAL>> parentConstraints) {
        if (csp.getDomain(root).isEmpty())
            return; // no solution!
        parentConstraints.put(root, null);
        orderedVars.add(root);
        int currParentIdx = -1;
        while (currParentIdx < orderedVars.size() - 1) {
            currParentIdx++;
            VAR currParent = orderedVars.get(currParentIdx);
            int arcsPointingUpwards = 0;
            for (Constraint<VAR, VAL> constraint : csp.getConstraints(currParent)) {
                VAR neighbor = csp.getNeighbor(currParent, constraint);
                if (neighbor == null)
                    return; // this constraint is not binary!
                if (parentConstraints.containsKey(neighbor)) {
                    arcsPointingUpwards++;
                    if (arcsPointingUpwards > 1)
                        return; // CSP is not a tree!
                } else {
                    parentConstraints.put(neighbor, constraint);
                    orderedVars.add(neighbor);
                }
            }
        }
    }

    private boolean makeArcConsistent(VAR xi, VAR xj, Constraint<VAR, VAL> constraint, CSP<VAR, VAL> csp) {
        boolean revised = false;
        Assignment<VAR, VAL> assignment = new Assignment<>();
        for (VAL vi : csp.getDomain(xi)) {
            assignment.add(xi, vi);
            boolean consistentExtensionFound = false;
            for (VAL vj : csp.getDomain(xj)) {
                assignment.add(xj, vj);
                if (constraint.isSatisfiedWith(assignment)) {
                    consistentExtensionFound = true;
                    break;
                }
            }
            if (!consistentExtensionFound) {
                csp.removeValueFromDomain(xi, vi);
                revised = true;
            }
        }
        return revised;
    }
}
