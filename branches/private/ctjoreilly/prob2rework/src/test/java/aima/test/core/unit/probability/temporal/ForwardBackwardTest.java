package aima.test.core.unit.probability.temporal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.bayes.model.FiniteBayesModel;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.temporal.ForwardBackward;
import aima.core.probability.util.ProbabilityTable;

public class ForwardBackwardTest {
	public static final double DELTA_THRESHOLD = 1e-3;
	//
	private ForwardBackward uw = null;

	@Before
	public void setUp() {
		Map<RandomVariable, RandomVariable> tToTm1StateVarMap = new HashMap<RandomVariable, RandomVariable>();
		tToTm1StateVarMap.put(ExampleRV.RAIN_t_RV, ExampleRV.RAIN_tm1_RV);
		uw = new ForwardBackward(getUmbrellaWorldTransitionModel(),
				tToTm1StateVarMap, getUmbrellaWorldSensorModel());
	}

	@Test
	public void testFoward() {
		//
		// Umbrella World

		// AIMA3e pg. 572
		// Day 0, no observations only the security guards prior beliefs
		// P(R<sub>0</sub>) = <0.5, 0.5>
		CategoricalDistribution prior = new ProbabilityTable(
				getUmbrellaWorldModel()
						.priorDistribution(ExampleRV.RAIN_tm1_RV).getValues(),
				ExampleRV.RAIN_t_RV);
		Assert.assertArrayEquals(new double[] { 0.5, 0.5 }, prior.getValues(),
				DELTA_THRESHOLD);

		// Day 1, the umbrella appears, so U<sub>1</sub> = true.
		// &asymp; <0.818, 0.182>
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));
		CategoricalDistribution f1 = uw.forward(prior, e1);
		Assert.assertArrayEquals(new double[] { 0.818, 0.182 }, f1.getValues(),
				DELTA_THRESHOLD);

		// Day 2, the umbrella appears, so U<sub>2</sub> = true.
		// &asymp; <0.883, 0.117>
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));
		CategoricalDistribution f2 = uw.forward(f1, e2);
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, f2.getValues(),
				DELTA_THRESHOLD);
	}

	@Test
	public void testBackward() {
		//
		// Umbrella World

		// AIMA3e pg. 575
		CategoricalDistribution b_kp2t = new ProbabilityTable(new double[] {
				1.0, 1.0 }, ExampleRV.RAIN_t_RV);
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));
		CategoricalDistribution b1 = uw.backward(b_kp2t, e2);
		Assert.assertArrayEquals(new double[] { 0.69, 0.41 }, b1.getValues(),
				DELTA_THRESHOLD);
	}

	@Test
	public void testForwardBackward() {
		//
		// Umbrella World

		CategoricalDistribution prior = new ProbabilityTable(
				getUmbrellaWorldModel()
						.priorDistribution(ExampleRV.RAIN_tm1_RV).getValues(),
				ExampleRV.RAIN_t_RV);
		Assert.assertArrayEquals(new double[] { 0.5, 0.5 }, prior.getValues(),
				DELTA_THRESHOLD);

		List<List<AssignmentProposition>> evidence = new ArrayList<List<AssignmentProposition>>();
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));
		evidence.add(e1);

		List<CategoricalDistribution> smoothed = uw.forwardBackward(evidence,
				prior);

		Assert.assertEquals(1, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.818, 0.182 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);

		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE));
		evidence.add(e2);

		smoothed = uw.forwardBackward(evidence, prior);

		Assert.assertEquals(2, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, smoothed.get(1)
				.getValues(), DELTA_THRESHOLD);

		List<AssignmentProposition> e3 = new ArrayList<AssignmentProposition>();
		e3.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));
		evidence.add(e3);

		smoothed = uw.forwardBackward(evidence, prior);

		Assert.assertEquals(3, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.861, 0.138 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.799, 0.201 }, smoothed.get(1)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.190, 0.810 }, smoothed.get(2)
				.getValues(), DELTA_THRESHOLD);
	}

	//
	// PRIVATE METHODS
	//
	private static FiniteProbabilityModel getUmbrellaWorldTransitionModel() {
		return getUmbrellaWorldModel();
	}

	private static FiniteProbabilityModel getUmbrellaWorldSensorModel() {
		return getUmbrellaWorldModel();
	}

	private static FiniteProbabilityModel getUmbrellaWorldModel() {
		// Prior belief state
		FiniteNode rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
				new double[] { 0.5, 0.5 });
		// Transition Model
		FiniteNode rain_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[] {
				// R_t-1 = true, R_t = true
				0.7,
				// R_t-1 = true, R_t = false
				0.3,
				// R_t-1 = false, R_t = true
				0.3,
				// R_t-1 = false, R_t = false
				0.7 }, rain_tm1);
		// Sensor Model
		@SuppressWarnings("unused")
		FiniteNode umbrealla_t = new FullCPTNode(ExampleRV.UMBREALLA_t_RV,
				new double[] {
						// R_t = true, U_t = true
						0.9,
						// R_t = true, U_t = false
						0.1,
						// R_t = false, U_t = true
						0.2,
						// R_t = false, U_t = false
						0.8 }, rain_t);

		return new FiniteBayesModel(new BayesNet(rain_tm1));
	}
}
