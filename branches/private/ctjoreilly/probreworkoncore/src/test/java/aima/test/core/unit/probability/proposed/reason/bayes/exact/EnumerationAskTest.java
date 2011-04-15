package aima.test.core.unit.probability.proposed.reason.bayes.exact;

import junit.framework.Assert;

import org.junit.Test;

import aima.core.probability.proposed.example.BayesNetExampleFactory;
import aima.core.probability.proposed.example.ExampleRV;
import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.bayes.BayesianNetwork;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;
import aima.core.probability.proposed.reason.bayes.exact.EnumerationAsk;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class EnumerationAskTest {
	//
	private EnumerationAsk enumerationAsk = new EnumerationAsk();

	@Test
	public void testInferenceOnToothacheCavityCatchNetwork() {
		BayesianNetwork bn = BayesNetExampleFactory
				.constructToothacheCavityCatchNetwork();

		Distribution d = enumerationAsk.enumerationAsk(
				new RandomVariable[] { ExampleRV.CAVITY_RV },
				new AssignmentProposition[] {}, bn);

		// System.out.println("P(Cavity)=" + d);
		Assert.assertEquals(2, d.getValues().length);
		Assert.assertEquals(0.2, d.getValues()[0],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
		Assert.assertEquals(0.8, d.getValues()[1],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);

		// AIMA3e pg. 493
		// P(Cavity | toothache) = <0.6, 0.4>
		d = enumerationAsk.enumerationAsk(
				new RandomVariable[] { ExampleRV.CAVITY_RV },
				new AssignmentProposition[] { new AssignmentProposition(
						ExampleRV.TOOTHACHE_RV, true) }, bn);

		// System.out.println("P(Cavity | toothache)=" + d);
		Assert.assertEquals(2, d.getValues().length);
		Assert.assertEquals(0.6, d.getValues()[0],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
		Assert.assertEquals(0.4, d.getValues()[1],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);

		// AIMA3e pg. 497
		// P(Cavity | toothache AND catch) = <0.871, 0.129>
		d = enumerationAsk
				.enumerationAsk(new RandomVariable[] { ExampleRV.CAVITY_RV },
						new AssignmentProposition[] {
								new AssignmentProposition(
										ExampleRV.TOOTHACHE_RV, true),
								new AssignmentProposition(ExampleRV.CATCH_RV,
										true) }, bn);

		// System.out.println("P(Cavity | toothache, catch)=" + d);
		Assert.assertEquals(2, d.getValues().length);
		Assert.assertEquals(0.8709677419354839, d.getValues()[0],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
		Assert.assertEquals(0.12903225806451615, d.getValues()[1],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
	}

	@Test
	public void testInferenceOnBurglaryAlarmNetwork() {
		BayesianNetwork bn = BayesNetExampleFactory
				.constructBurglaryAlarmNetwork();

		// AIMA3e. pg. 514
		Distribution d = enumerationAsk
				.enumerationAsk(new RandomVariable[] { ExampleRV.ALARM_RV },
						new AssignmentProposition[] {
								new AssignmentProposition(
										ExampleRV.BURGLARY_RV, false),
								new AssignmentProposition(
										ExampleRV.EARTHQUAKE_RV, false),
								new AssignmentProposition(
										ExampleRV.JOHN_CALLS_RV, true),
								new AssignmentProposition(
										ExampleRV.MARY_CALLS_RV, true) }, bn);

		// System.out.println("P(Alarm | ~b, ~e, j, m)=" + d);
		Assert.assertEquals(2, d.getValues().length);
		Assert.assertEquals(0.5577689243027888, d.getValues()[0],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
		Assert.assertEquals(0.44223107569721115, d.getValues()[1],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);

		// AIMA3e pg. 523
		// P(Burglary | JohnCalls = true, MaryCalls = true) = <0.284, 0.716>
		d = enumerationAsk
				.enumerationAsk(new RandomVariable[] { ExampleRV.BURGLARY_RV },
						new AssignmentProposition[] {
								new AssignmentProposition(
										ExampleRV.JOHN_CALLS_RV, true),
								new AssignmentProposition(
										ExampleRV.MARY_CALLS_RV, true) }, bn);

		// System.out.println("P(Burglary | j, m)=" + d);
		Assert.assertEquals(2, d.getValues().length);
		Assert.assertEquals(0.2841718353643929, d.getValues()[0],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
		Assert.assertEquals(0.7158281646356071, d.getValues()[1],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);

		// AIMA3e pg. 528
		// P(JohnCalls | Burglary = true)
		d = enumerationAsk.enumerationAsk(
				new RandomVariable[] { ExampleRV.JOHN_CALLS_RV },
				new AssignmentProposition[] { new AssignmentProposition(
						ExampleRV.BURGLARY_RV, true) }, bn);
		// System.out.println("P(JohnCalls | b)=" + d);
		Assert.assertEquals(2, d.getValues().length);
		Assert.assertEquals(0.8490169999999999, d.getValues()[0],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
		Assert.assertEquals(0.15098299999999998, d.getValues()[1],
				ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD);
	}
}
