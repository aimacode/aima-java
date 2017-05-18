package aima.gui.demo.search;

import aima.core.search.csp.*;
import aima.core.search.csp.examples.MapCSP;

/**
 * Demonstrates the performance of different constraint solving strategies.
 * The map coloring problem from the textbook is used as CSP.
 * 
 * @author Ruediger Lunde
 */

public class MapColoringCspDemo {
	public static void main(String[] args) {
		CSP<Variable, String> csp = new MapCSP();
		StepCounter stepCounter = new StepCounter();
		CspSolver<Variable, String> solver;
		
		solver = new MinConflictsSolver<>(1000);
		solver.addCspStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Minimum Conflicts)");
		System.out.println(solver.solve(csp));
		System.out.println(stepCounter.getResults() + "\n");
		
		solver = new FlexibleBacktrackingSolver<Variable, String>().setAll();
		solver.addCspStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Backtracking + MRV & DEG + LCV + AC3)");
		System.out.println(solver.solve(csp));
		System.out.println(stepCounter.getResults() + "\n");
		
		solver = new FlexibleBacktrackingSolver<>();
		solver.addCspStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Backtracking)");
		System.out.println(solver.solve(csp));
		System.out.println(stepCounter.getResults() + "\n");
	}
	
	/** Counts assignment and domain changes during CSP solving. */
	protected static class StepCounter implements CspListener<Variable, String> {
		private int assignmentCount = 0;
		private int domainCount = 0;
		
		@Override
		public void stateChanged(Assignment<Variable, String> assignment, CSP<Variable, String> csp) {
			++assignmentCount;
		}
		
		@Override
		public void stateChanged(CSP<Variable, String> csp) {
			++domainCount;
		}
		
		public void reset() {
			assignmentCount = 0;
			domainCount = 0;
		}
		
		public String getResults() {
			StringBuilder result = new StringBuilder();
			result.append("assignment changes: ").append(assignmentCount);
			if (domainCount != 0)
				result.append(", domain changes: ").append(domainCount);
			return result.toString();
		}
	}
}
