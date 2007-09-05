package aima.test.search.searches;

import junit.framework.TestCase;
import aima.search.informed.SimulatedAnnealingSearch;

public class SimulatedAnnealingSearchTest extends TestCase {

	public void testForGivenNegativeDeltaEProbabilityOfAcceptanceDecreasesWithDecreasingTemperature() {
		// this isn't very nice. the object's state is uninitialized but is ok
		// for this test.
		SimulatedAnnealingSearch search = new SimulatedAnnealingSearch();
		int deltaE = -1;
		double higherTemperature = 30.0;
		double lowerTemperature = 29.5;

		assertTrue(search.probabilityOfAcceptance(lowerTemperature, deltaE) < search
				.probabilityOfAcceptance(higherTemperature, deltaE));
	}

}
