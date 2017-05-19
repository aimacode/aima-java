package aima.gui.fx.applications.search;

import aima.core.search.csp.*;
import aima.core.search.csp.examples.NQueensCSP;

public class NQueensCspDemo {
	public static void main(String[] args) {
		int size = 64;
		CSP<Variable, Integer> csp = new NQueensCSP(size);
		CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();
		CspSolver<Variable, Integer> solver;
		
		System.out.println(size + "-Queens (Min-Conflicts)");
		solver = new MinConflictsSolver<>(1000);
		solver.addCspListener(stepCounter);
		stepCounter.reset();
		Assignment<Variable, Integer> sol = solver.solve(csp);
		System.out.println((sol.isSolution(csp) ? ":-) " : ":-( ") + sol);
		System.out.println(stepCounter.getResults() + "\n");
		
		System.out.println(size + "-Queens (Backtracking + MRV & DEG + LCV + AC3)");
		solver = new FlexibleBacktrackingSolver<Variable, Integer>().setAll();
		solver.addCspListener(stepCounter);
		stepCounter.reset();
		System.out.println(solver.solve(csp));
		System.out.println(stepCounter.getResults() + "\n");
		
		
		size = 16;
		csp = new NQueensCSP(size);
		System.out.println(size + "-Queens (Backtracking)");
		solver = new FlexibleBacktrackingSolver<>();
		solver.addCspListener(stepCounter);
		stepCounter.reset();
		System.out.println(solver.solve(csp));
		System.out.println(stepCounter.getResults() + "\n");
	}
}
