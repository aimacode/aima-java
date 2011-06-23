package aima.core.probability;

import aima.core.probability.bayes.approx.BayesInferenceApproxAdapter;
import aima.core.probability.bayes.approx.GibbsAsk;
import aima.core.probability.bayes.approx.LikelihoodWeighting;
import aima.core.probability.bayes.approx.RejectionSampling;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.exact.EnumerationAsk;
import aima.core.probability.bayes.model.FiniteBayesModel;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.example.FullJointDistributionBurglaryAlarmModel;
import aima.core.probability.example.FullJointDistributionToothacheCavityCatchModel;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.DisjunctiveProposition;

// TODO-this is to be written and then moved to the aima-gui project.
/**
 * 
 * @author Ciaran O'Reilly
 */
public class ProbabilityDemo {
	// Note: You should increase this to 1000000+
	// in order to get answers from the approximate
	// algorithms (i.e. Rejection, Likelihood and Gibbs)
	// that look close to their exact inference
	// counterparts.
	public static final int NUM_SAMPLES = 1000;

	public static void main(String[] args) {
		fullJointDistributionModelDemo();

		bayesEnumerationAskDemo();
		bayesEliminationAskDemo();

		bayesRejectionSamplingDemo();
		bayesLikelihoodWeightingDemo();
		bayesGibbsAskDemo();
	}

	public static void fullJointDistributionModelDemo() {
		System.out.println("DEMO: Full Joint Distribution Model");
		System.out.println("===================================");
		demoToothacheCavityCatchModel(new FullJointDistributionToothacheCavityCatchModel());
		demoBurglaryAlarmModel(new FullJointDistributionBurglaryAlarmModel());
		System.out.println("===================================");
	}

	public static void bayesEnumerationAskDemo() {
		System.out.println("DEMO: Bayes Enumeration Ask");
		System.out.println("===========================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new EnumerationAsk()));
		demoBurglaryAlarmModel(new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork(), new EnumerationAsk()));
		System.out.println("===========================");
	}

	public static void bayesEliminationAskDemo() {
		System.out.println("DEMO: Bayes Elimination Ask");
		System.out.println("===========================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new EliminationAsk()));
		demoBurglaryAlarmModel(new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork(), new EliminationAsk()));
		System.out.println("===========================");
	}

	public static void bayesRejectionSamplingDemo() {
		System.out.println("DEMO: Bayes Rejection Sampling N = "+NUM_SAMPLES);
		System.out.println("==============================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new BayesInferenceApproxAdapter(new RejectionSampling(),
						NUM_SAMPLES)));
		demoBurglaryAlarmModel(new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork(),
				new BayesInferenceApproxAdapter(new RejectionSampling(),
						NUM_SAMPLES)));
		System.out.println("==============================");
	}

	public static void bayesLikelihoodWeightingDemo() {
		System.out.println("DEMO: Bayes Likelihood Weighting N = "+NUM_SAMPLES);
		System.out.println("================================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new BayesInferenceApproxAdapter(new LikelihoodWeighting(),
						NUM_SAMPLES)));
		demoBurglaryAlarmModel(new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork(),
				new BayesInferenceApproxAdapter(new LikelihoodWeighting(),
						NUM_SAMPLES)));
		System.out.println("================================");
	}

	public static void bayesGibbsAskDemo() {
		System.out.println("DEMO: Bayes Gibbs Ask N = "+NUM_SAMPLES);
		System.out.println("=====================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new BayesInferenceApproxAdapter(new GibbsAsk(), NUM_SAMPLES)));
		demoBurglaryAlarmModel(new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork(),
				new BayesInferenceApproxAdapter(new GibbsAsk(), NUM_SAMPLES)));
		System.out.println("=====================");
	}

	//
	// PRIVATE METHODS
	//
	private static void demoToothacheCavityCatchModel(
			FiniteProbabilityModel model) {
		System.out.println("Toothache, Cavity, and Catch Model");
		System.out.println("----------------------------------");
		AssignmentProposition atoothache = new AssignmentProposition(
				ExampleRV.TOOTHACHE_RV, Boolean.TRUE);
		AssignmentProposition acavity = new AssignmentProposition(
				ExampleRV.CAVITY_RV, Boolean.TRUE);
		AssignmentProposition anotcavity = new AssignmentProposition(
				ExampleRV.CAVITY_RV, Boolean.FALSE);
		AssignmentProposition acatch = new AssignmentProposition(
				ExampleRV.CATCH_RV, Boolean.TRUE);

		// AIMA3e pg. 485
		System.out.println("P(cavity) = " + model.prior(acavity));
		System.out.println("P(cavity | toothache) = "
				+ model.posterior(acavity, atoothache));

		// AIMA3e pg. 492
		DisjunctiveProposition cavityOrToothache = new DisjunctiveProposition(
				acavity, atoothache);
		System.out.println("P(cavity OR toothache) = "
				+ model.prior(cavityOrToothache));

		// AIMA3e pg. 493
		System.out.println("P(~cavity | toothache) = "
				+ model.posterior(anotcavity, atoothache));

		// AIMA3e pg. 493
		// P<>(Cavity | toothache) = <0.6, 0.4>
		System.out.println("P<>(Cavity | toothache) = "
				+ model.posteriorDistribution(ExampleRV.CAVITY_RV, atoothache));

		// AIMA3e pg. 497
		// P<>(Cavity | toothache AND catch) = <0.871, 0.129>
		System.out.println("P<>(Cavity | toothache AND catch) = "
				+ model.posteriorDistribution(ExampleRV.CAVITY_RV, atoothache,
						acatch));
	}

	private static void demoBurglaryAlarmModel(FiniteProbabilityModel model) {
		System.out.println("--------------------");
		System.out.println("Burglary Alarm Model");
		System.out.println("--------------------");

		AssignmentProposition aburglary = new AssignmentProposition(
				ExampleRV.BURGLARY_RV, Boolean.TRUE);
		AssignmentProposition anotburglary = new AssignmentProposition(
				ExampleRV.BURGLARY_RV, Boolean.FALSE);
		AssignmentProposition anotearthquake = new AssignmentProposition(
				ExampleRV.EARTHQUAKE_RV, Boolean.FALSE);
		AssignmentProposition aalarm = new AssignmentProposition(
				ExampleRV.ALARM_RV, Boolean.TRUE);
		AssignmentProposition anotalarm = new AssignmentProposition(
				ExampleRV.ALARM_RV, Boolean.FALSE);
		AssignmentProposition ajohnCalls = new AssignmentProposition(
				ExampleRV.JOHN_CALLS_RV, Boolean.TRUE);
		AssignmentProposition amaryCalls = new AssignmentProposition(
				ExampleRV.MARY_CALLS_RV, Boolean.TRUE);

		// AIMA3e pg. 514
		System.out.println("P(j,m,a,~b,~e) = "
				+ model.prior(ajohnCalls, amaryCalls, aalarm, anotburglary,
						anotearthquake));
		System.out.println("P(j,m,~a,~b,~e) = "
				+ model.prior(ajohnCalls, amaryCalls, anotalarm, anotburglary,
						anotearthquake));

		// AIMA3e. pg. 514
		// P<>(Alarm | JohnCalls = true, MaryCalls = true, Burglary = false,
		// Earthquake = false)
		// = <0.558, 0.442>
		System.out
				.println("P<>(Alarm | JohnCalls = true, MaryCalls = true, Burglary = false, Earthquake = false) = "
						+ model.posteriorDistribution(ExampleRV.ALARM_RV,
								ajohnCalls, amaryCalls, anotburglary,
								anotearthquake));

		// AIMA3e pg. 523
		// P<>(Burglary | JohnCalls = true, MaryCalls = true) = <0.284, 0.716>
		System.out
				.println("P<>(Burglary | JohnCalls = true, MaryCalls = true) = "
						+ model.posteriorDistribution(ExampleRV.BURGLARY_RV,
								ajohnCalls, amaryCalls));

		// AIMA3e pg. 528
		// P<>(JohnCalls | Burglary = true)
		System.out.println("P<>(JohnCalls | Burglary = true) = "
				+ model.posteriorDistribution(ExampleRV.JOHN_CALLS_RV,
						aburglary));
	}
}
