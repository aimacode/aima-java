package aima.test.core.unit.logic.fol.inference;

import org.junit.Test;

import aima.core.logic.fol.inference.FOLModelElimination;
import aima.test.core.unit.logic.fol.CommonFOLInferenceProcedureTests;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLModelEliminationTest extends CommonFOLInferenceProcedureTests {

	@Test
	public void testDefiniteClauseKBKingsQueryCriminalXFalse() {
		testDefiniteClauseKBKingsQueryCriminalXFalse(new FOLModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryRichardEvilFalse() {
		testDefiniteClauseKBKingsQueryRichardEvilFalse(new FOLModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryJohnEvilSucceeds() {
		testDefiniteClauseKBKingsQueryJohnEvilSucceeds(new FOLModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds() {
		testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(new FOLModelElimination());
	}

	@Test
	public void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds() {
		testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(new FOLModelElimination());
	}

	@Test
	public void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds() {
		testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(new FOLModelElimination());
	}

	@Test
	public void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew() {
		// This KB ends up being infinite when resolving, however 2
		// seconds is more than enough to extract the 4 answers
		// that are expected
		testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(new FOLModelElimination(
				2 * 1000));
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
				new FOLModelElimination(), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds() {
		testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
				new FOLModelElimination(), false);
	}

	@Test
	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// Note: While the KB expands infinitely, the answer
		// search for this bottoms out indicating the
		// KB does not entail the fact.
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(
				new FOLModelElimination(), false);
	}

	@Test
	public void testEqualityAxiomsKBabcAEqualsCSucceeds() {
		testEqualityAxiomsKBabcAEqualsCSucceeds(new FOLModelElimination());
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(new FOLModelElimination());
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(new FOLModelElimination());
	}

	@Test
	public void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
				new FOLModelElimination(), false);
	}

	@Test
	public void testEqualityNoAxiomsKBabcAEqualsCSucceeds() {
		testEqualityNoAxiomsKBabcAEqualsCSucceeds(new FOLModelElimination(),
				true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdFFASucceeds(
				new FOLModelElimination(), true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPDSucceeds(
				new FOLModelElimination(), true);
	}

	@Test
	public void testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds() {
		testEqualityAndSubstitutionNoAxiomsKBabcdPFFASucceeds(
				new FOLModelElimination(), true);
	}
}