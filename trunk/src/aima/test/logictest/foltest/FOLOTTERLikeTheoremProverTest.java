package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;

import aima.logic.fol.CNFConverter;
import aima.logic.fol.domain.FOLDomain;
import aima.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.logic.fol.inference.otter.defaultimpl.DefaultClauseSimplifier;
import aima.logic.fol.kb.data.CNF;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.TermEquality;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLOTTERLikeTheoremProverTest extends
		CommonFOLInferenceProcedureTests {

	public void testDefaultClauseSimplifier() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("ZERO");
		domain.addConstant("ONE");
		domain.addPredicate("P");
		domain.addFunction("Plus");
		domain.addFunction("Power");

		FOLParser parser = new FOLParser(domain);

		List<TermEquality> rewrites = new ArrayList<TermEquality>();
		rewrites.add((TermEquality) parser.parse("Plus(x, ZERO) = x"));
		rewrites.add((TermEquality) parser.parse("Plus(ZERO, x) = x"));
		rewrites.add((TermEquality) parser.parse("Power(x, ONE) = x"));
		rewrites.add((TermEquality) parser.parse("Power(x, ZERO) = ONE"));
		DefaultClauseSimplifier simplifier = new DefaultClauseSimplifier(
				rewrites);

		Sentence s1 = parser
				.parse("((P(Plus(y,ZERO),Plus(ZERO,y)) OR P(Power(y, ONE),Power(y,ZERO))) OR P(Power(y,ZERO),Plus(y,ZERO)))");

		CNFConverter cnfConverter = new CNFConverter(parser);

		CNF cnf = cnfConverter.convertToCNF(s1);

		assertEquals(1, cnf.getNumberOfClauses());

		Clause simplified = simplifier.simplify(cnf.getConjunctionOfClauses()
				.get(0));

		assertEquals("[P(y,y), P(y,ONE), P(ONE,y)]", simplified.toString());
	}

	public void testDefiniteClauseKBKingsQueryCriminalXFalse() {
		testDefiniteClauseKBKingsQueryCriminalXFalse(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testDefiniteClauseKBKingsQueryRichardEvilFalse() {
		testDefiniteClauseKBKingsQueryRichardEvilFalse(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew() {
		// This KB ends up being infinite when resolving, however 2
		// seconds is more than enough to extract the 4 answers
		// that are expected
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(new FOLOTTERLikeTheoremProver(
				2 * 1000, false));
	}

	public void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
				new FOLOTTERLikeTheoremProver(false), false);
	}

	public void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
				new FOLOTTERLikeTheoremProver(false), false);
	}

	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// This query will not return using OTTER Like resolution
		// as keep expanding clauses through resolution for this KB.
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(
				new FOLOTTERLikeTheoremProver(false), true);
	}

	public void testEqualityAxiomsKBabcAEqualsCSucceeds() {
		testEqualityAxiomsKBabcAEqualsCSucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds() {
		xtestEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}

	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
				new FOLOTTERLikeTheoremProver(false), false);
	}

	public void testEqualityNoAxiomsKBabcAEqualsCSucceeds() {
		testEqualityNoAxiomsKBabcAEqualsCSucceeds(
				new FOLOTTERLikeTheoremProver(true), false);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
				new FOLOTTERLikeTheoremProver(true), false);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
				new FOLOTTERLikeTheoremProver(true), false);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
				new FOLOTTERLikeTheoremProver(true), false);
	}
}