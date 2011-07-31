package aima.test.core.unit.probability.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import aima.core.probability.util.RandVar;

public class ProbabilityTableTest {
	public static final double DELTA_THRESHOLD = ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;

	@Test
	public void test_divideBy() {
		RandomVariable xRV = new RandVar("X", new BooleanDomain());
		RandomVariable yRV = new RandVar("Y", new BooleanDomain());
		RandomVariable zRV = new RandVar("Z", new BooleanDomain());

		ProbabilityTable xyzD = new ProbabilityTable(new double[] {
				// X = true, Y = true, Z = true
				1.0,
				// X = true, Y = true, Z = false
				2.0,
				// X = true, Y = false, Z = true
				3.0,
				// X = true, Y = false, Z = false
				4.0,
				// X = false, Y = true, Z = true
				5.0,
				// X = false, Y = true, Z = false
				6.0,
				// X = false, Y = false, Z = true
				7.0,
				// X = false, Y = false, Z = false
				8.0, }, xRV, yRV, zRV);
		ProbabilityTable xzyD = new ProbabilityTable(new double[] {
				// X = true, Z = true, Y = true
				1.0,
				// X = true, Z = true, Y = false
				3.0,
				// X = true, Z = false, Y = true
				2.0,
				// X = true, Z = false, Y = false
				4.0,
				// X = false, Z = true, Y = true
				5.0,
				// X = false, Z = true, Y = false
				7.0,
				// X = false, Z = false, Y = true
				6.0,
				// X = false, Z = false, Y = false
				8.0, }, xRV, zRV, yRV);
		ProbabilityTable zxyD = new ProbabilityTable(new double[] {
				// Z = true, X = true, Y = true
				1.0,
				// Z = true, X = true, Y = false
				3.0,
				// Z = true, X = false, Y = true
				5.0,
				// Z = true, X = false, Y = false
				7.0,
				// Z = false, X = true, Y = true
				2.0,
				// Z = false, X = true, Y = false
				4.0,
				// Z = false, X = false, Y = true
				6.0,
				// Z = false, X = false, Y = false
				8.0, }, zRV, xRV, yRV);
		ProbabilityTable zD = new ProbabilityTable(new double[] { 0.5, 0.2 },
				zRV);
		// The identity distribution (to order results for comparison purposes)
		ProbabilityTable iD = new ProbabilityTable(new double[] { 1.0 });
		// Ensure the order of the dividends
		// makes no difference to the result
		Assert.assertArrayEquals(xyzD.divideBy(zD).getValues(),
				xzyD.divideBy(zD).pointwiseProductPOS(iD, xRV, yRV, zRV)
						.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(xzyD.divideBy(zD).getValues(),
				zxyD.divideBy(zD).pointwiseProductPOS(iD, xRV, zRV, yRV)
						.getValues(), DELTA_THRESHOLD);
	}

	@Test
	public void test_pointwiseProduct() {
		RandomVariable xRV = new RandVar("X", new BooleanDomain());
		RandomVariable yRV = new RandVar("Y", new BooleanDomain());
		RandomVariable zRV = new RandVar("Z", new BooleanDomain());

		ProbabilityTable xyD = new ProbabilityTable(new double[] {
				// X = true, Y = true
				1.0,
				// X = true, Y = false
				2.0,
				// X = false, Y = true
				3.0,
				// X = false, Y = false
				4.0 }, xRV, yRV);
		ProbabilityTable zD = new ProbabilityTable(new double[] { 3.0, 7.0 },
				zRV);

		// Not commutative
		Assert.assertArrayEquals(new double[] { 3.0, 7.0, 6.0, 14.0, 9.0, 21.0,
				12.0, 28.0 }, xyD.pointwiseProduct(zD).getValues(),
				DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 3.0, 6.0, 9.0, 12.0, 7.0, 14.0,
				21.0, 28.0 }, zD.pointwiseProduct(xyD).getValues(),
				DELTA_THRESHOLD);
	}

	@Test
	public void test_pointwiseProductPOS() {
		RandomVariable xRV = new RandVar("X", new BooleanDomain());
		RandomVariable yRV = new RandVar("Y", new BooleanDomain());
		RandomVariable zRV = new RandVar("Z", new BooleanDomain());

		ProbabilityTable xyD = new ProbabilityTable(new double[] {
				// X = true, Y = true
				1.0,
				// X = true, Y = false
				2.0,
				// X = false, Y = true
				3.0,
				// X = false, Y = false
				4.0 }, xRV, yRV);
		ProbabilityTable zD = new ProbabilityTable(new double[] { 3.0, 7.0 },
				zRV);

		// Make commutative by specifying an order for the product
		Assert.assertArrayEquals(xyD.pointwiseProduct(zD).getValues(), zD
				.pointwiseProductPOS(xyD, xRV, yRV, zRV).getValues(),
				DELTA_THRESHOLD);
	}

	@Test
	public void test_iterateOverTable_fixedValues() {
		RandVar aRV = new RandVar("A", new BooleanDomain());
		RandVar bRV = new RandVar("B", new BooleanDomain());
		RandVar cRV = new RandVar("C", new BooleanDomain());
		ProbabilityTable ptABC = new ProbabilityTable(new double[] {
				// A = true, B = true, C = true
				1.0,
				// A = true, B = true, C = false
				10.0,
				// A = true, B = false, C = true
				100.0,
				// A = true, B = false, C = false
				1000.0,
				// A = false, B = true, C = true
				10000.0,
				// A = false, B = true, C = false
				100000.0,
				// A = false, B = false, C = true
				1000000.0,
				// A = false, B = false, C = false
				10000000.0 }, aRV, bRV, cRV);

		final List<Double> answer = new ArrayList<Double>();
		ProbabilityTable.Iterator pti = new ProbabilityTable.Iterator() {

			@Override
			public void iterate(Map<RandomVariable, Object> possibleAssignment,
					double probability) {
				answer.add(probability);
			}
		};

		answer.clear();
		ptABC.iterateOverTable(pti, new AssignmentProposition(aRV, true));
		Assert.assertEquals(1111.0, sumOf(answer), DELTA_THRESHOLD);

		answer.clear();
		ptABC.iterateOverTable(pti, new AssignmentProposition(aRV, false));
		Assert.assertEquals(11110000.0, sumOf(answer), DELTA_THRESHOLD);

		answer.clear();
		ptABC.iterateOverTable(pti, new AssignmentProposition(bRV, true));
		Assert.assertEquals(110011.0, sumOf(answer), DELTA_THRESHOLD);

		answer.clear();
		ptABC.iterateOverTable(pti, new AssignmentProposition(bRV, false));
		Assert.assertEquals(11001100.0, sumOf(answer), DELTA_THRESHOLD);

		answer.clear();
		ptABC.iterateOverTable(pti, new AssignmentProposition(cRV, true));
		Assert.assertEquals(1010101.0, sumOf(answer), DELTA_THRESHOLD);

		answer.clear();
		ptABC.iterateOverTable(pti, new AssignmentProposition(cRV, false));
		Assert.assertEquals(10101010.0, sumOf(answer), DELTA_THRESHOLD);

		answer.clear();
		ptABC.iterateOverTable(pti, new AssignmentProposition(bRV, true),
				new AssignmentProposition(cRV, true));
		Assert.assertEquals(10001.0, sumOf(answer), DELTA_THRESHOLD);
	}

	//
	// PRIVATE METHOD
	//
	private double sumOf(List<Double> values) {
		double sum = 0;
		for (Double d : values) {
			sum += d;
		}
		return sum;
	}
}
