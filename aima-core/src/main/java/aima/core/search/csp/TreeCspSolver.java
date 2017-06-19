package aima.core.search.csp;

import aima.core.util.Util;

import java.util.*;

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
 * previous loop, this will only happen if n == 1.
 *
 * @author Ruediger Lunde
 * @author Anurag Rai
 */
public class TreeCspSolver<VAR extends Variable, VAL> extends CspSolver<VAR, VAL> {

    private boolean useRandom;

    public TreeCspSolver<VAR, VAL> useRandom(boolean b) {
        useRandom = b;
        return this;
    }

    @Override
    public Optional<Assignment<VAR, VAL>> solve(CSP<VAR, VAL> csp) {

        Assignment<VAR, VAL> assignment = new Assignment<>();
        // Select a root from the List of Variables
        VAR root = useRandom ? Util.selectRandomlyFromList(csp.getVariables()) : csp.getVariables().get(0);
        // Sort the variables in topological order
        List<VAR> orderedVars = new ArrayList<>();
        Map<VAR, Constraint<VAR, VAL>> parentConstraints = new HashMap<>();
        topologicalSort(csp, root, orderedVars, parentConstraints);
        if (csp.getDomain(root).isEmpty())
            return Optional.empty(); // CSP has no solution! (needed if orderedVars.size() == 1)

        // Establish arc consistency from top to bottom (starting at the bottom).
        csp = csp.copyDomains(); // do not change the original CSP!
        for (int i = orderedVars.size() - 1; i > 0; i--) {
            VAR var = orderedVars.get(i);
            Constraint<VAR, VAL> constraint = parentConstraints.get(var);
            VAR parent = csp.getNeighbor(var, constraint);
            if (makeArcConsistent(parent, var, constraint, csp)) {
                fireStateChanged(csp, null, parent);
                if (csp.getDomain(parent).isEmpty())
                    return Optional.empty(); // CSP has no solution!
            }
        }

        // Assign values to variables from top to bottom.
        for (int i = 0; i < orderedVars.size(); i++) {
            VAR var = orderedVars.get(i);
            for (VAL value : csp.getDomain(var)) {
                assignment.add(var, value);
                if (assignment.isConsistent(csp.getConstraints(var))) {
                    fireStateChanged(csp, assignment, var);
                    break;
                }
            }
        }
        return Optional.of(assignment);
    }

    /**
     * Computes an explicit representation of the tree structure and a total order which is consistent with the
     * parent-child relations. If the provided CSP has not the required properties (CSP contains only binary
     * constraints, constraint graph is tree-structured and connected), an exception is thrown.
     *
     * @param csp               A CSP
     * @param root              A root variable
     * @param orderedVars       The computed total order (initially empty)
     * @param parentConstraints The tree structure, maps a variable to the constraint representing the arc to the parent
     *                          variable (initially empty)
     */
    private void topologicalSort(CSP<VAR, VAL> csp, VAR root, List<VAR> orderedVars,
                                 Map<VAR, Constraint<VAR, VAL>> parentConstraints) {
        orderedVars.add(root);
        parentConstraints.put(root, null);
        int currParentIdx = -1;
        while (currParentIdx < orderedVars.size() - 1) {
            currParentIdx++;
            VAR currParent = orderedVars.get(currParentIdx);
            int arcsPointingUpwards = 0;
            for (Constraint<VAR, VAL> constraint : csp.getConstraints(currParent)) {
                VAR neighbor = csp.getNeighbor(currParent, constraint);
                if (neighbor == null)
                    throw new IllegalArgumentException("Constraint " + constraint + " is not binary.");
                if (parentConstraints.containsKey(neighbor)) { // faster than orderedVars.contains(neighbor)!
                    arcsPointingUpwards++;
                    if (arcsPointingUpwards > 1)
                        throw new IllegalArgumentException("CSP is not tree-structured.");
                } else {
                    orderedVars.add(neighbor);
                    parentConstraints.put(neighbor, constraint);
                }
            }
        }
        if (orderedVars.size() < csp.getVariables().size())
            throw new IllegalArgumentException("Constraint graph is not connected.");
    }

    /**
     * Establishes arc-consistency for (xi, xj).
     * @return value true if the domain of xi was reduced.
     */
    private boolean makeArcConsistent(VAR xi, VAR xj, Constraint<VAR, VAL> constraint, CSP<VAR, VAL> csp) {
        Domain<VAL> currDomain = csp.getDomain(xi);
        List<VAL> newValues = new ArrayList<>(currDomain.size());
        Assignment<VAR, VAL> assignment = new Assignment<>();
        for (VAL vi : currDomain) {
            assignment.add(xi, vi);
            for (VAL vj : csp.getDomain(xj)) {
                assignment.add(xj, vj);
                if (constraint.isSatisfiedWith(assignment)) {
                    newValues.add(vi);
                    break;
                }
            }
        }
        if (newValues.size() < currDomain.size()) {
            csp.setDomain(xi, new Domain<>(newValues));
            return true;
        }
        return false;
    }
}
