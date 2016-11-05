package aima.gui.demo.probability;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.bayes.approx.BayesInferenceApproxAdapter;
import aima.core.probability.bayes.approx.GibbsAsk;
import aima.core.probability.bayes.approx.LikelihoodWeighting;
import aima.core.probability.bayes.approx.ParticleFiltering;
import aima.core.probability.bayes.approx.RejectionSampling;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.exact.EnumerationAsk;
import aima.core.probability.bayes.model.FiniteBayesModel;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.example.DynamicBayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.example.FullJointDistributionBurglaryAlarmModel;
import aima.core.probability.example.FullJointDistributionToothacheCavityCatchModel;
import aima.core.probability.example.GenericTemporalModelFactory;
import aima.core.probability.example.HMMExampleFactory;
import aima.core.probability.example.MDPFactory;
import aima.core.probability.hmm.exact.FixedLagSmoothing;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.Policy;
import aima.core.probability.mdp.impl.ModifiedPolicyEvaluation;
import aima.core.probability.mdp.search.PolicyIteration;
import aima.core.probability.mdp.search.ValueIteration;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.DisjunctiveProposition;
import aima.core.probability.temporal.generic.ForwardBackward;
import aima.core.probability.util.ProbabilityTable;
import aima.core.util.MockRandomizer;

/**
 * @author Ravi Mohan
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
		// Chapter 13
		fullJointDistributionModelDemo();

		// Chapter 14 - Exact
		bayesEnumerationAskDemo();
		bayesEliminationAskDemo();
		// Chapter 14 - Approx
		bayesRejectionSamplingDemo();
		bayesLikelihoodWeightingDemo();
		bayesGibbsAskDemo();

		// Chapter 15
		forwardBackWardDemo();
		fixedLagSmoothingDemo();
		particleFilterinfDemo();

		// Chapter 17
		valueIterationDemo();
		policyIterationDemo();
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
		demoBurglaryAlarmModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructBurglaryAlarmNetwork(),
				new EnumerationAsk()));
		System.out.println("===========================");
	}

	public static void bayesEliminationAskDemo() {
		System.out.println("DEMO: Bayes Elimination Ask");
		System.out.println("===========================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new EliminationAsk()));
		demoBurglaryAlarmModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructBurglaryAlarmNetwork(),
				new EliminationAsk()));
		System.out.println("===========================");
	}

	public static void bayesRejectionSamplingDemo() {
		System.out.println("DEMO: Bayes Rejection Sampling N = " + NUM_SAMPLES);
		System.out.println("==============================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new BayesInferenceApproxAdapter(new RejectionSampling(),
						NUM_SAMPLES)));
		demoBurglaryAlarmModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructBurglaryAlarmNetwork(),
				new BayesInferenceApproxAdapter(new RejectionSampling(),
						NUM_SAMPLES)));
		System.out.println("==============================");
	}

	public static void bayesLikelihoodWeightingDemo() {
		System.out.println("DEMO: Bayes Likelihood Weighting N = "
				+ NUM_SAMPLES);
		System.out.println("================================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new BayesInferenceApproxAdapter(new LikelihoodWeighting(),
						NUM_SAMPLES)));
		demoBurglaryAlarmModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructBurglaryAlarmNetwork(),
				new BayesInferenceApproxAdapter(new LikelihoodWeighting(),
						NUM_SAMPLES)));
		System.out.println("================================");
	}

	public static void bayesGibbsAskDemo() {
		System.out.println("DEMO: Bayes Gibbs Ask N = " + NUM_SAMPLES);
		System.out.println("=====================");
		demoToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork(),
				new BayesInferenceApproxAdapter(new GibbsAsk(), NUM_SAMPLES)));
		demoBurglaryAlarmModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructBurglaryAlarmNetwork(),
				new BayesInferenceApproxAdapter(new GibbsAsk(), NUM_SAMPLES)));
		System.out.println("=====================");
	}

	public static void forwardBackWardDemo() {

		System.out.println("DEMO: Forward-BackWard");
		System.out.println("======================");

		System.out.println("Umbrella World");
		System.out.println("--------------");
		ForwardBackward uw = new ForwardBackward(
				GenericTemporalModelFactory.getUmbrellaWorldTransitionModel(),
				GenericTemporalModelFactory.getUmbrellaWorld_Xt_to_Xtm1_Map(),
				GenericTemporalModelFactory.getUmbrellaWorldSensorModel());

		CategoricalDistribution prior = new ProbabilityTable(new double[] {
				0.5, 0.5 }, ExampleRV.RAIN_t_RV);

		// Day 1
		List<List<AssignmentProposition>> evidence = new ArrayList<List<AssignmentProposition>>();
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));
		evidence.add(e1);

		List<CategoricalDistribution> smoothed = uw.forwardBackward(evidence,
				prior);

		System.out.println("Day 1 (Umbrealla_t=true) smoothed:\nday 1 = "
				+ smoothed.get(0));

		// Day 2
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));
		evidence.add(e2);

		smoothed = uw.forwardBackward(evidence, prior);

		System.out.println("Day 2 (Umbrealla_t=true) smoothed:\nday 1 = "
				+ smoothed.get(0) + "\nday 2 = " + smoothed.get(1));

		// Day 3
		List<AssignmentProposition> e3 = new ArrayList<AssignmentProposition>();
		e3.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));
		evidence.add(e3);

		smoothed = uw.forwardBackward(evidence, prior);

		System.out.println("Day 3 (Umbrealla_t=false) smoothed:\nday 1 = "
				+ smoothed.get(0) + "\nday 2 = " + smoothed.get(1)
				+ "\nday 3 = " + smoothed.get(2));

		System.out.println("======================");
	}

	public static void fixedLagSmoothingDemo() {
		System.out.println("DEMO: Fixed-Lag-Smoothing");
		System.out.println("=========================");
		System.out.println("Lag = 1");
		System.out.println("-------");
		FixedLagSmoothing uw = new FixedLagSmoothing(
				HMMExampleFactory.getUmbrellaWorldModel(), 1);

		// Day 1 - Lag 1
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));

		CategoricalDistribution smoothed = uw.fixedLagSmoothing(e1);

		System.out.println("Day 1 (Umbrella_t=true) smoothed:\nday 1="
				+ smoothed);

		// Day 2 - Lag 1
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));

		smoothed = uw.fixedLagSmoothing(e2);

		System.out.println("Day 2 (Umbrella_t=true) smoothed:\nday 1="
				+ smoothed);

		// Day 3 - Lag 1
		List<AssignmentProposition> e3 = new ArrayList<AssignmentProposition>();
		e3.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));

		smoothed = uw.fixedLagSmoothing(e3);

		System.out.println("Day 3 (Umbrella_t=false) smoothed:\nday 2="
				+ smoothed);

		System.out.println("-------");
		System.out.println("Lag = 2");
		System.out.println("-------");

		uw = new FixedLagSmoothing(HMMExampleFactory.getUmbrellaWorldModel(), 2);

		// Day 1 - Lag 2
		e1 = new ArrayList<AssignmentProposition>();
		e1.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));

		smoothed = uw.fixedLagSmoothing(e1);

		System.out.println("Day 1 (Umbrella_t=true) smoothed:\nday 1="
				+ smoothed);

		// Day 2 - Lag 2
		e2 = new ArrayList<AssignmentProposition>();
		e2.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));

		smoothed = uw.fixedLagSmoothing(e2);

		System.out.println("Day 2 (Umbrella_t=true) smoothed:\nday 1="
				+ smoothed);

		// Day 3 - Lag 2
		e3 = new ArrayList<AssignmentProposition>();
		e3.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));

		smoothed = uw.fixedLagSmoothing(e3);

		System.out.println("Day 3 (Umbrella_t=false) smoothed:\nday 1="
				+ smoothed);

		System.out.println("=========================");
	}

	public static void particleFilterinfDemo() {
		System.out.println("DEMO: Particle-Filtering");
		System.out.println("========================");
		System.out.println("Figure 15.18");
		System.out.println("------------");

		MockRandomizer mr = new MockRandomizer(new double[] {
				// Prior Sample:
				// 8 with Rain_t-1=true from prior distribution
				0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,
				// 2 with Rain_t-1=false from prior distribution
				0.6, 0.6,
				// (a) Propagate 6 samples Rain_t=true
				0.7, 0.7, 0.7, 0.7, 0.7, 0.7,
				// 4 samples Rain_t=false
				0.71, 0.71, 0.31, 0.31,
				// (b) Weight should be for first 6 samples:
				// Rain_t-1=true, Rain_t=true, Umbrella_t=false = 0.1
				// Next 2 samples:
				// Rain_t-1=true, Rain_t=false, Umbrealla_t=false= 0.8
				// Final 2 samples:
				// Rain_t-1=false, Rain_t=false, Umbrella_t=false = 0.8
				// gives W[] =
				// [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.8, 0.8, 0.8, 0.8]
				// normalized =
				// [0.026, ...., 0.211, ....] is approx. 0.156 = true
				// the remainder is false
				// (c) Resample 2 Rain_t=true, 8 Rain_t=false
				0.15, 0.15, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2,
				//
				// Next Sample:
				// (a) Propagate 1 samples Rain_t=true
				0.7,
				// 9 samples Rain_t=false
				0.71, 0.31, 0.31, 0.31, 0.31, 0.31, 0.31, 0.31, 0.31,
				// (c) resample 1 Rain_t=true, 9 Rain_t=false
				0.0001, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2 });

		int N = 10;
		ParticleFiltering pf = new ParticleFiltering(N,
				DynamicBayesNetExampleFactory.getUmbrellaWorldNetwork(), mr);

		AssignmentProposition[] e = new AssignmentProposition[] { new AssignmentProposition(
				ExampleRV.UMBREALLA_t_RV, false) };

		System.out.println("First Sample Set:");
		AssignmentProposition[][] S = pf.particleFiltering(e);
		for (int i = 0; i < N; i++) {
			System.out.println("Sample " + (i + 1) + " = " + S[i][0]);
		}
		System.out.println("Second Sample Set:");
		S = pf.particleFiltering(e);
		for (int i = 0; i < N; i++) {
			System.out.println("Sample " + (i + 1) + " = " + S[i][0]);
		}

		System.out.println("========================");
	}

	public static void valueIterationDemo() {

		System.out.println("DEMO: Value Iteration");
		System.out.println("=====================");
		System.out.println("Figure 17.3");
		System.out.println("-----------");

		CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
		MarkovDecisionProcess<Cell<Double>, CellWorldAction> mdp = MDPFactory
				.createMDPForFigure17_3(cw);
		ValueIteration<Cell<Double>, CellWorldAction> vi = new ValueIteration<Cell<Double>, CellWorldAction>(
				1.0);

		Map<Cell<Double>, Double> U = vi.valueIteration(mdp, 0.0001);

		System.out.println("(1,1) = " + U.get(cw.getCellAt(1, 1)));
		System.out.println("(1,2) = " + U.get(cw.getCellAt(1, 2)));
		System.out.println("(1,3) = " + U.get(cw.getCellAt(1, 3)));

		System.out.println("(2,1) = " + U.get(cw.getCellAt(2, 1)));
		System.out.println("(2,3) = " + U.get(cw.getCellAt(2, 3)));

		System.out.println("(3,1) = " + U.get(cw.getCellAt(3, 1)));
		System.out.println("(3,2) = " + U.get(cw.getCellAt(3, 2)));
		System.out.println("(3,3) = " + U.get(cw.getCellAt(3, 3)));

		System.out.println("(4,1) = " + U.get(cw.getCellAt(4, 1)));
		System.out.println("(4,2) = " + U.get(cw.getCellAt(4, 2)));
		System.out.println("(4,3) = " + U.get(cw.getCellAt(4, 3)));

		System.out.println("=========================");
	}

	public static void policyIterationDemo() {

		System.out.println("DEMO: Policy Iteration");
		System.out.println("======================");
		System.out.println("Figure 17.3");
		System.out.println("-----------");

		CellWorld<Double> cw = CellWorldFactory.createCellWorldForFig17_1();
		MarkovDecisionProcess<Cell<Double>, CellWorldAction> mdp = MDPFactory
				.createMDPForFigure17_3(cw);
		PolicyIteration<Cell<Double>, CellWorldAction> pi = new PolicyIteration<Cell<Double>, CellWorldAction>(
				new ModifiedPolicyEvaluation<Cell<Double>, CellWorldAction>(50,
						1.0));

		Policy<Cell<Double>, CellWorldAction> policy = pi.policyIteration(mdp);

		System.out.println("(1,1) = " + policy.action(cw.getCellAt(1, 1)));
		System.out.println("(1,2) = " + policy.action(cw.getCellAt(1, 2)));
		System.out.println("(1,3) = " + policy.action(cw.getCellAt(1, 3)));

		System.out.println("(2,1) = " + policy.action(cw.getCellAt(2, 1)));
		System.out.println("(2,3) = " + policy.action(cw.getCellAt(2, 3)));

		System.out.println("(3,1) = " + policy.action(cw.getCellAt(3, 1)));
		System.out.println("(3,2) = " + policy.action(cw.getCellAt(3, 2)));
		System.out.println("(3,3) = " + policy.action(cw.getCellAt(3, 3)));

		System.out.println("(4,1) = " + policy.action(cw.getCellAt(4, 1)));
		System.out.println("(4,2) = " + policy.action(cw.getCellAt(4, 2)));
		System.out.println("(4,3) = " + policy.action(cw.getCellAt(4, 3)));

		System.out.println("=========================");
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
