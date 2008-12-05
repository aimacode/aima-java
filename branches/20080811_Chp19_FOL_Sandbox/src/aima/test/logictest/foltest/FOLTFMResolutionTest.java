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
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(new FOLTFMResolution(
				10 * 1000), false);
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
	
	public void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new FOLTFMResolution(
				10 * 1000));
	}
	
	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(new FOLTFMResolution(
				10 * 1000));
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