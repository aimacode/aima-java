package aima.test.regression.logic.fol;

import aima.logic.fol.inference.FOLTFMResolution;
import aima.test.logictest.foltest.CommonFOLInferenceProcedureTests;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLTFMResolutionRegression extends
		CommonFOLInferenceProcedureTests {
	
	public void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse() {
		// This query will not return using TFM as keep expanding
		// clauses through resolution for this KB.
		testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(new FOLTFMResolution(
				1000 * 1000), true);
	}
}