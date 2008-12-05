package aima.test.logictest.foltest;

import aima.logic.fol.inference.FOLOTTERLikeTheoremProver;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLOTTERLikeTheoremProverTest extends CommonFOLInferenceProcedureTests {

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
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(
				new FOLOTTERLikeTheoremProver(2 * 1000, false), false);
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
		testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}
	
	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(new FOLOTTERLikeTheoremProver(
				false));
	}
	
	public void testEqualityNoAxiomsKBabcAEqualsCSucceeds() {
		testEqualityNoAxiomsKBabcAEqualsCSucceeds(new FOLOTTERLikeTheoremProver(
				true), false);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(new FOLOTTERLikeTheoremProver(
				true), false);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(new FOLOTTERLikeTheoremProver(
				true), false);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(new FOLOTTERLikeTheoremProver(
				true), false);
	}
}