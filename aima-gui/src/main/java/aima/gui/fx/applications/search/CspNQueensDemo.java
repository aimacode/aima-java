package aima.gui.fx.applications.search;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.examples.NQueensCSP;
import aima.core.search.framework.Metrics;

public class CspNQueensDemo {
	public static void main(String[] args) {
		int size = 64;
		CSP csp = new NQueensCSP(size);
		StepCounter stepCounter = new StepCounter();
		SolutionStrategy solver;
		
		System.out.println(size + "-Queens (Min-Conflicts)");
		solver = new MinConflictsStrategy(1000);
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		Assignment sol = solver.solve(csp.copyDomains());
		System.out.println((sol.isSolution(csp) ? ":-) " : ":-( ") + sol);
		System.out.println(stepCounter.getResults() + "\n");
		
		System.out.println(size + "-Queens (Backtracking + MRV + DEG + AC3 + LCV)");
		solver = new ImprovedBacktrackingStrategy(true, true, true, true);
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		System.out.println(solver.solve(csp.copyDomains()));
		System.out.println(stepCounter.getResults() + "\n");
		
		
		size = 16;
		csp = new NQueensCSP(size);
		System.out.println(size + "-Queens (Backtracking)");
		solver = new BacktrackingStrategy();
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		System.out.println(solver.solve(csp.copyDomains()));
		System.out.println(stepCounter.getResults() + "\n");
	}
	
	
	protected static class StepCounter implements CSPStateListener {
		private int assignmentCount = 0;
		private int domainCount = 0;
		
		@Override
		public void stateChanged(Assignment assignment, CSP csp) {
			++assignmentCount;
		}
		
		@Override
		public void stateChanged(CSP csp) {
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
