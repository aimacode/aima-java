package aima.gui.demo.search;

import aima.core.search.csp.*;
import aima.core.search.csp.examples.SudokuCSP;
import aima.core.search.csp.solver.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SudokuCspDemo {

    private static int stepCounterOverall = 0;

    public static void main(String[] args) {
        SudokuCSP csp = new SudokuCSP();
        CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();
        CspSolver<Variable, Integer> solver;
        Optional<Assignment<Variable, Integer>> solution;

        solver = new FlexibleBacktrackingSolver<Variable, Integer>().setAll();
        solver.addCspListener(stepCounter);
        stepCounter.reset();
        System.out.println("Sudoku (Backtracking + MRV & DEG + LCV + AC3)");

        solver.addCspListener((csp1, assignment, var) -> {
            stepCounterOverall++;
            print(csp1, assignment, var);
        });

        solution = solver.solve(csp, csp.getStartingAssignment());
        solution.ifPresent(System.out::println);
        System.out.println("Step Counter results: " + stepCounter.getResults() + "\n");
    }

    private static void print(CSP<Variable, Integer> csp, Assignment<Variable, Integer> assignment, Variable var) {
        Map<String, Integer> assignments = new HashMap<>();

        String txt1 = "Step " + stepCounterOverall + ": ";
        String txt2 = "Domain reduced";
        if (assignment != null) {
            for (Variable variable : assignment.getVariables()) {
                assignments.put(variable.getName(), assignment.getValue(variable));
            }

            SudokuCSP.printCSP(assignments);

            if (var != null)
                txt2 = "Assignment changed, " + var + " = " + assignment.getValue(var);
            else
                txt2 = "Assignment changed, " + assignment.toString();
            if (assignment.isSolution(csp))
                txt2 += " (Solution)";
        } else {
            txt2 = "Domain reduced" + (var != null ? ", Dom(" + var + ") = " + csp.getDomain(var) + ")" : "");
        }
        System.out.println(txt1 + txt2 + "\n");
    }
}
