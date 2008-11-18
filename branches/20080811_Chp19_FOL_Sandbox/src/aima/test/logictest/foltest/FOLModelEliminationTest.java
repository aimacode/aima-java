package aima.test.logictest.foltest;

import aima.logic.fol.inference.FOLModelElimination;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLModelEliminationTest extends CommonFOLInferenceProcedureTests {

	public void testDefiniteClauseKBKingsQueryCriminalXFails() {
		testDefiniteClauseKBKingsQueryCriminalXFails(new FOLModelElimination());
	}
	
	public void testDefiniteClauseKBKingsQueryRichardEvilFails() {
		testDefiniteClauseKBKingsQueryRichardEvilFails(new FOLModelElimination());
	}

	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new FOLModelElimination());
	}

	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new FOLModelElimination());
	}
	
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new FOLModelElimination());
	}

	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new FOLModelElimination());
	}
	
	public void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew() {
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(
				new FOLModelElimination(), false);
	}

	public void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
				new FOLModelElimination(), false);
	}

	public void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
				new FOLModelElimination(), false);
	}
}