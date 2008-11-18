package aima.test.logictest.foltest;

import aima.logic.fol.inference.FOLFCAsk;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLFCAskTest extends CommonFOLInferenceProcedureTests {

	public void testDefiniteClauseKBKingsQueryCriminalXFails() {
		testDefiniteClauseKBKingsQueryCriminalXFails(new FOLFCAsk());
	}

	public void testDefiniteClauseKBKingsQueryRichardEvilFails() {
		testDefiniteClauseKBKingsQueryRichardEvilFails(new FOLFCAsk());
	}

	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new FOLFCAsk());
	}

	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new FOLFCAsk());
	}
	
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new FOLFCAsk());
	}

	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new FOLFCAsk());
	}
}