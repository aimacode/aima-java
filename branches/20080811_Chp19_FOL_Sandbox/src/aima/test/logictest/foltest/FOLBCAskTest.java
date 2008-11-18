package aima.test.logictest.foltest;

import aima.logic.fol.inference.FOLBCAsk;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLBCAskTest extends CommonFOLInferenceProcedureTests {

	public void testDefiniteClauseKBKingsQueryCriminalXFails() {
		testDefiniteClauseKBKingsQueryCriminalXFails(new FOLBCAsk());
	}
	
	public void testDefiniteClauseKBKingsQueryRichardEvilFails() {
		testDefiniteClauseKBKingsQueryRichardEvilFails(new FOLBCAsk());
	}

	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new FOLBCAsk());
	}

	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new FOLBCAsk());
	}
	
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new FOLBCAsk());
	}

	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new FOLBCAsk());
	}
}