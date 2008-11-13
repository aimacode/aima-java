package aima.test.regression.logic.fol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import aima.logic.fol.inference.FOLTFMResolution;
import aima.logic.fol.inference.trace.FOLTFMResolutionTracer;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLTFMResolutionRegression extends TestCase {
	
	public void testComplexFullFOLResolutionTruthResponseJackIsNotTheKillerSucceeds() {
		FOLKnowledgeBase akb = createLovesAnimalKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Jack"));
		terms.add(new Constant("Tuna"));
		Predicate query = new Predicate("Kills", terms);

		Set<Map<Variable, Term>> answer = akb.ask(query);
		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(0, answer.iterator().next().size());
	}

	//
	// PRIVATE
	//
	private FOLKnowledgeBase createLovesAnimalKnowledgeBase() {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory
				.lovesAnimalDomain(), new FOLTFMResolution(
				new RegressionFOLTFMResolutionTracer()));

		kb
				.tell("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");
		kb
				.tell("FORALL x (EXISTS y (Animal(y) AND Kills(x, y)) => FORALL z NOT(Loves(z, x)))");
		kb.tell("FORALL x (Animal(x) => Loves(Jack, x))");
		kb.tell("(Kills(Jack, Tuna) OR Kills(Curiosity, Tuna))");
		kb.tell("Cat(Tuna)");
		kb.tell("FORALL x (Cat(x) => Animal(x))");

		return kb;
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
				Set<Map<Variable, Term>> result) {
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