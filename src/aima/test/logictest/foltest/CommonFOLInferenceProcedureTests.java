package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import aima.logic.fol.inference.InferenceProcedure;
import aima.logic.fol.inference.InferenceResult;
import aima.logic.fol.inference.proof.Proof;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class CommonFOLInferenceProcedureTests extends TestCase {

	//
	// Protected Methods
	//
	protected void testDefiniteClauseKBKingsQueryCriminalXFalse(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = FOLKnowledgeBaseFactory
				.createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);
		InferenceResult answer = kkb.ask(query);
		assertTrue(null != answer);
		assertTrue(answer.isPossiblyFalse());
		assertFalse(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(0 == answer.getProofs().size());
	}

	protected void testDefiniteClauseKBKingsQueryRichardEvilFalse(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = FOLKnowledgeBaseFactory
				.createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Richard"));
		Predicate query = new Predicate("Evil", terms);
		InferenceResult answer = kkb.ask(query);
		assertTrue(null != answer);
		assertTrue(answer.isPossiblyFalse());
		assertFalse(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(0 == answer.getProofs().size());
	}

	protected void testDefiniteClauseKBKingsQueryJohnEvilSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = FOLKnowledgeBaseFactory
				.createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("John"));
		Predicate query = new Predicate("Evil", terms);
		InferenceResult answer = kkb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(1 == answer.getProofs().size());
		assertTrue(0 == answer.getProofs().get(0).getAnswerBindings().size());
	}

	protected void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = FOLKnowledgeBaseFactory
				.createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Evil", terms);
		InferenceResult answer = kkb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(1 == answer.getProofs().size());
		assertTrue(1 == answer.getProofs().get(0).getAnswerBindings().size());
		assertEquals(new Constant("John"), answer.getProofs().get(0)
				.getAnswerBindings().get(new Variable("x")));
	}

	protected void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = FOLKnowledgeBaseFactory
				.createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("King", terms);
		InferenceResult answer = kkb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(2 == answer.getProofs().size());
		assertTrue(1 == answer.getProofs().get(0).getAnswerBindings().size());
		assertTrue(1 == answer.getProofs().get(1).getAnswerBindings().size());

		boolean gotJohn, gotRichard;
		gotJohn = gotRichard = false;
		Constant cJohn = new Constant("John");
		Constant cRichard = new Constant("Richard");
		for (Proof p : answer.getProofs()) {
			Map<Variable, Term> ans = p.getAnswerBindings();
			assertEquals(1, ans.size());
			if (cJohn.equals(ans.get(new Variable("x")))) {
				gotJohn = true;
			}
			if (cRichard.equals(ans.get(new Variable("x")))) {
				gotRichard = true;
			}
		}
		assertTrue(gotJohn);
		assertTrue(gotRichard);
	}

	protected void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase wkb = FOLKnowledgeBaseFactory
				.createWeaponsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);

		InferenceResult answer = wkb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(1 == answer.getProofs().size());
		assertTrue(1 == answer.getProofs().get(0).getAnswerBindings().size());
		assertEquals(new Constant("West"), answer.getProofs().get(0)
				.getAnswerBindings().get(new Variable("x")));
	}

	protected void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(
			InferenceProcedure infp) {
		FOLKnowledgeBase rotkb = FOLKnowledgeBaseFactory
				.createRingOfThievesKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Skis", terms);

		InferenceResult answer = rotkb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		// DB can expand infinitely so is only partial.
		assertTrue(answer.isPartialResultDueToTimeout());
		assertTrue(4 == answer.getProofs().size());
		assertTrue(1 == answer.getProofs().get(0).getAnswerBindings().size());
		assertTrue(1 == answer.getProofs().get(1).getAnswerBindings().size());
		assertTrue(1 == answer.getProofs().get(2).getAnswerBindings().size());
		assertTrue(1 == answer.getProofs().get(3).getAnswerBindings().size());

		List<Constant> expected = new ArrayList<Constant>();
		expected.add(new Constant("Nancy"));
		expected.add(new Constant("Red"));
		expected.add(new Constant("Bert"));
		expected.add(new Constant("Drew"));
		for (Proof p : answer.getProofs()) {
			expected.remove(p.getAnswerBindings().get(new Variable("x")));
		}
		assertEquals(0, expected.size());
	}

	protected void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createLovesAnimalKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Curiosity"));
		terms.add(new Constant("Tuna"));
		Predicate query = new Predicate("Kills", terms);

		InferenceResult answer = akb.ask(query);
		assertTrue(null != answer);
		if (expectedToTimeOut) {
			assertFalse(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertTrue(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertFalse(answer.isPossiblyFalse());
			assertTrue(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(1 == answer.getProofs().size());
			assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
					.size());
		}
	}

	protected void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createLovesAnimalKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Jack"));
		terms.add(new Constant("Tuna"));
		NotSentence query = new NotSentence(new Predicate("Kills", terms));

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		if (expectedToTimeOut) {
			assertFalse(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertTrue(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertFalse(answer.isPossiblyFalse());
			assertTrue(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(1 == answer.getProofs().size());
			assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
					.size());
		}
	}

	protected void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createLovesAnimalKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Jack"));
		terms.add(new Constant("Tuna"));
		Predicate query = new Predicate("Kills", terms);

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		if (expectedToTimeOut) {
			assertFalse(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertTrue(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertTrue(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		}
	}

	protected void testEqualityAxiomsKBabcAEqualsCSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCEqualityKnowledgeBase(infp, true);

		TermEquality query = new TermEquality(new Constant("A"), new Constant(
				"C"));

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(1 == answer.getProofs().size());
		assertTrue(0 == answer.getProofs().get(0).getAnswerBindings().size());
	}

	protected void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, true);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("A"));
		Function fa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(fa);
		TermEquality query = new TermEquality(new Function("F", terms),
				new Constant("A"));

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(1 == answer.getProofs().size());
		assertTrue(0 == answer.getProofs().get(0).getAnswerBindings().size());
	}

	protected void xtestEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, true);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("D"));
		Predicate query = new Predicate("P", terms);

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		assertFalse(answer.isPossiblyFalse());
		assertTrue(answer.isTrue());
		assertFalse(answer.isUnknownDueToTimeout());
		assertFalse(answer.isPartialResultDueToTimeout());
		assertTrue(1 == answer.getProofs().size());
		assertTrue(0 == answer.getProofs().get(0).getAnswerBindings().size());
	}

	protected void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, true);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("A"));
		Function fa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(fa);
		Function ffa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(ffa);
		Predicate query = new Predicate("P", terms);

		InferenceResult answer = akb.ask(query);

		if (expectedToTimeOut) {
			assertFalse(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertTrue(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertTrue(null != answer);
			assertFalse(answer.isPossiblyFalse());
			assertTrue(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(1 == answer.getProofs().size());
			assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
					.size());
		}
	}

	protected void testEqualityNoAxiomsKBabcAEqualsCSucceeds(
			InferenceProcedure infp, boolean expectedToFail) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCEqualityKnowledgeBase(infp, false);

		TermEquality query = new TermEquality(new Constant("A"), new Constant(
				"C"));

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		if (expectedToFail) {
			assertTrue(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertFalse(answer.isPossiblyFalse());
			assertTrue(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(1 == answer.getProofs().size());
			assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
					.size());
		}
	}

	protected void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
			InferenceProcedure infp, boolean expectedToFail) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, false);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("A"));
		Function fa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(fa);
		TermEquality query = new TermEquality(new Function("F", terms),
				new Constant("A"));

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		if (expectedToFail) {
			assertTrue(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertFalse(answer.isPossiblyFalse());
			assertTrue(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(1 == answer.getProofs().size());
			assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
					.size());
		}
	}

	protected void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
			InferenceProcedure infp, boolean expectedToFail) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, false);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("D"));
		Predicate query = new Predicate("P", terms);

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		if (expectedToFail) {
			assertTrue(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertFalse(answer.isPossiblyFalse());
			assertTrue(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(1 == answer.getProofs().size());
			assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
					.size());
		}
	}

	protected void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
			InferenceProcedure infp, boolean expectedToFail) {
		FOLKnowledgeBase akb = FOLKnowledgeBaseFactory
				.createABCDEqualityAndSubstitutionKnowledgeBase(infp, false);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("A"));
		Function fa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(fa);
		Function ffa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(ffa);
		Predicate query = new Predicate("P", terms);

		InferenceResult answer = akb.ask(query);

		assertTrue(null != answer);
		if (expectedToFail) {
			assertTrue(answer.isPossiblyFalse());
			assertFalse(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(0 == answer.getProofs().size());
		} else {
			assertFalse(answer.isPossiblyFalse());
			assertTrue(answer.isTrue());
			assertFalse(answer.isUnknownDueToTimeout());
			assertFalse(answer.isPartialResultDueToTimeout());
			assertTrue(1 == answer.getProofs().size());
			assertTrue(0 == answer.getProofs().get(0).getAnswerBindings()
					.size());
		}
	}
}