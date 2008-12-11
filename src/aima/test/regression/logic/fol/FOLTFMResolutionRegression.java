package aima.test.regression.logic.fol;

import java.util.Set;

import aima.logic.fol.inference.FOLTFMResolution;
import aima.logic.fol.inference.InferenceResult;
import aima.logic.fol.inference.trace.FOLTFMResolutionTracer;
import aima.logic.fol.kb.data.Clause;
import aima.test.logictest.foltest.CommonFOLInferenceProcedureTests;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLTFMResolutionRegression extends
		CommonFOLInferenceProcedureTests {
	
	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// This query will not return using TFM as keep expanding
		// clauses through resolution for this KB.
		FOLTFMResolution ip = new FOLTFMResolution(1000 * 1000);
		ip.setTracer(new RegressionFOLTFMResolutionTracer());
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(ip, true);
	}
	
	private class RegressionFOLTFMResolutionTracer implements
			FOLTFMResolutionTracer {
		private int outerCnt = 1;
		private int noPairsConsidered = 0;
		private int noPairsResolved = 0;
		private int maxClauseSizeSeen = 0;

		public void stepStartWhile(Set<Clause> clauses, int totalNoClauses,
				int totalNoNewCandidateClauses) {
			outerCnt = 1;

			System.out.println("");
			System.out.println("Total # clauses=" + totalNoClauses
					+ ", total # new candidate clauses="
					+ totalNoNewCandidateClauses);
		}

		public void stepOuterFor(Clause i) {
			System.out.print(" " + outerCnt);
			if (outerCnt % 50 == 0) {
				System.out.println("");
			}
			outerCnt++;
		}

		public void stepInnerFor(Clause i, Clause j) {
			noPairsConsidered++;
		}

		public void stepResolved(Clause iFactor, Clause jFactor,
				Set<Clause> resolvents) {
			noPairsResolved++;

			Clause egLargestClause = null;
			for (Clause c : resolvents) {
				if (c.getNumberLiterals() > maxClauseSizeSeen) {
					egLargestClause = c;
					maxClauseSizeSeen = c.getNumberLiterals();
				}
			}
			if (null != egLargestClause) {
				System.out.println("");
				System.out.println("E.g. largest clause so far="
						+ maxClauseSizeSeen + ", " + egLargestClause);
				System.out.println("i=" + iFactor);
				System.out.println("j=" + jFactor);
			}
		}

		public void stepFinished(Set<Clause> clauses,
				InferenceResult result) {
			System.out.println("Total # Pairs of Clauses Considered:"
					+ noPairsConsidered);
			System.out.println("Total # Pairs of Clauses Resolved  :"
					+ noPairsResolved);
			noPairsConsidered = 0;
			noPairsResolved = 0;
			maxClauseSizeSeen = 0;
		}
	}
}