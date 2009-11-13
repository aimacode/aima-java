package aima.test.core.unit.search.local;

import org.junit.Assert;
import org.junit.Test;

import aima.core.search.local.SimulatedAnnealingSearch;

public class SimulatedAnnealingSearchTest {

	@Test
	public void testForGivenNegativeDeltaEProbabilityOfAcceptanceDecreasesWithDecreasingTemperature() {
		// this isn't very nice. the object's state is uninitialized but is ok
		// for this test.
		SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(null);
		int deltaE = -1;
		double higherTemperature = 30.0;
		double lowerTemperature = 29.5;

		Assert.assertTrue(search.probabilityOfAcceptance(lowerTemperature,
				deltaE) < search.probabilityOfAcceptance(higherTemperature,
				deltaE));
	}

}
