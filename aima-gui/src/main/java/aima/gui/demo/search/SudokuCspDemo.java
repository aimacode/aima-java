package aima.gui.demo.search;

import aima.core.environment.sudoku.SudokuDifficulty;
import aima.core.search.csp.*;
import aima.core.search.csp.examples.SudokuCSP;
import aima.core.search.csp.solver.*;

public class SudokuCspDemo {

    private static int stepCounterOverall = 0;
    private static long startTime;

    public static void main(String[] args) {
        SudokuCSP csp = new SudokuCSP();
        CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();
        CspSolver<Variable, Integer> solver;

        solver = new FlexibleBacktrackingSolver<Variable, Integer>().setAll();
        solver.addCspListener(stepCounter);
        stepCounter.reset();

        solver.addCspListener((csp1, assignment, var) -> {
            stepCounterOverall++;
            print(csp1, assignment, var);
        });

        System.out.println("Sudoku:");
        Assignment<Variable, Integer> startAssignment = csp.getStartingAssignment(SudokuDifficulty.HARD);
        SudokuCSP.printCSP(startAssignment);

        System.out.println("Hint: the variables are composed as follows [row|col]. So 24 corresponds to row=2, col=3.");
        System.out.println("Difficulty level: HARD");
        System.out.println("Solver: Backtracking + AC3 + MRV&DEG + LCV");
        System.out.println();
        System.out.println("Solving Sudoku CSP ...");
        csp.setDomainsForStartingAssignment(startAssignment);
        stepCounterOverall = 0;
        startTime = System.nanoTime();
        solver.solve(csp, startAssignment);
        System.out.println();
        System.out.println("Steps overall: " + stepCounterOverall);
        System.out.println("Step Counter results: " + stepCounter.getResults());
    }

    /**
     * This method is for printing intermediate steps of the Sudoku CSP solving problem.
     */
    private static void print(CSP<Variable, Integer> csp, Assignment<Variable, Integer> assignment, Variable var) {
        String txt1 = "Step " + stepCounterOverall + ": ";
        String txt2 = "Domain reduced";

        if (assignment != null) {
            if (var != null)
                txt2 = "Assignment changed, " + var + " = " + assignment.getValue(var);
            else
                txt2 = "Assignment changed, " + assignment.toString();
            if (assignment.isSolution(csp))
                txt2 += " (Solution in " + ((System.nanoTime() - startTime) / 1000000) + "ms)";
        } else {
            txt2 = "Domain reduced" + (var != null ? ", Dom(" + var + ") = " + csp.getDomain(var) + ")" : "");
        }
        System.out.println(txt1 + txt2);
        if (assignment != null && assignment.isSolution(csp)) {
            System.out.println("\nSolution:");
            SudokuCSP.printCSP(assignment);
        }
    }
}
