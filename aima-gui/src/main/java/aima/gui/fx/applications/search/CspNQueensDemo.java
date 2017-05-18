package aima.gui.fx.applications.search;

import aima.core.search.csp.*;
import aima.core.search.csp.examples.NQueensCSP;
import aima.core.search.framework.Metrics;

public class CspNQueensDemo {
	public static void main(String[] args) {
		int size = 64;
		CSP<Variable, Integer> csp = new NQueensCSP(size);
		StepCounter stepCounter = new StepCounter();
		CspSolver<Variable, Integer> solver;
		
		System.out.println(size + "-Queens (Min-Conflicts)");
		solver = new MinConflictsSolver<>(1000);
		solver.addCspStateListener(stepCounter);
		stepCounter.reset();
		Assignment<Variable, Integer> sol = solver.solve(csp.copyDomains());
		System.out.println((sol.isSolution(csp) ? ":-) " : ":-( ") + sol);
		System.out.println(stepCounter.getResults() + "\n");
		
		System.out.println(size + "-Queens (Backtracking + MRV & DEG + LCV + AC3)");
		solver = new BacktrackingSolver<Variable, Integer>().setAll();
		solver.addCspStateListener(stepCounter);
		stepCounter.reset();
		System.out.println(solver.solve(csp.copyDomains()));
		System.out.println(stepCounter.getResults() + "\n");
		
		
		size = 16;
		csp = new NQueensCSP(size);
		System.out.println(size + "-Queens (Backtracking)");
		solver = new BacktrackingSolver<>();
		solver.addCspStateListener(stepCounter);
		stepCounter.reset();
		System.out.println(solver.solve(csp.copyDomains()));
		System.out.println(stepCounter.getResults() + "\n");
	}
	
	
	protected static class StepCounter implements CspListener<Variable, Integer> {
		private int assignmentCount = 0;
		private int domainCount = 0;
		
		@Override
		public void stateChanged(Assignment<Variable, Integer> assignment, CSP<Variable, Integer> csp) {
			++assignmentCount;
		}
		
		@Override
		public void stateChanged(CSP<Variable, Integer> csp) {
			++domainCount;
		}
		
		public void reset() {
			assignmentCount = 0;
			domainCount = 0;
		}
		
		public Metrics getResults() {
			Metrics result = new Metrics();
			result.set("assignmentChanges", assignmentCount);
			if (domainCount != 0)
				result.set("domainChanges", domainCount);
			return result;
		}
	}
}
