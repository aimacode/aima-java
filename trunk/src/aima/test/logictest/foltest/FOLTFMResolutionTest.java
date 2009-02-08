package aima.test.logictest.foltest;

import aima.logic.fol.inference.FOLTFMResolution;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLTFMResolutionTest extends CommonFOLInferenceProcedureTests {

	public void testDefiniteClauseKBKingsQueryCriminalXFalse() {
		testDefiniteClauseKBKingsQueryCriminalXFalse(new FOLTFMResolution());
	}

	public void testDefiniteClauseKBKingsQueryRichardEvilFalse() {
		testDefiniteClauseKBKingsQueryRichardEvilFalse(new FOLTFMResolution());
	}

	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new FOLTFMResolution());
	}

	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new FOLTFMResolution());
	}

	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new FOLTFMResolution());
	}

	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new FOLTFMResolution());
	}

	public void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew() {
		// The clauses in this KB can keep creating resolvents infinitely,
		// therefore give it 10 seconds to find the 4 answers to this, should
		// be more than enough.
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(new FOLTFMResolution(
				10 * 1000));
	}

	public void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds() {
		// 10 seconds should be more than plenty for this query to finish.
		testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
				new FOLTFMResolution(10 * 1000), false);
	}

	public void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds() {
		// 10 seconds should be more than plenty for this query to finish.
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
				new FOLTFMResolution(10 * 1000), false);
	}

	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// This query will not return using TFM as keep expanding
		// clauses through resolution for this KB.
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(new FOLTFMResolution(
				10 * 1000), true);
	}

	public void testEqualityAxiomsKBabcAEqualsCSucceeds() {
		testEqualityAxiomsKBabcAEqualsCSucceeds(new FOLTFMResolution(10 * 1000));
	}

	public void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(new FOLTFMResolution(
				10 * 1000));
	}

	// Note: Requires VM arguments to be:
	// -Xms256m -Xmx1024m
	// due to the amount of memory it uses.
	public void xtestEqualityAndSubstitutionAxiomsKBabcdPDSucceeds() {
		xtestEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new FOLTFMResolution(
				10 * 1000));
	}

	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		// TFM is unable to find the correct answer to this in a reasonable
		// amount of time for a JUnit test.
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
				new FOLTFMResolution(10 * 1000), true);
	}

	public void testEqualityNoAxiomsKBabcAEqualsCSucceeds() {
		testEqualityNoAxiomsKBabcAEqualsCSucceeds(new FOLTFMResolution(
				10 * 1000), true);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
				new FOLTFMResolution(10 * 1000), true);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
				new FOLTFMResolution(10 * 1000), true);
	}

	public void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
				new FOLTFMResolution(10 * 1000), true);
	}
}