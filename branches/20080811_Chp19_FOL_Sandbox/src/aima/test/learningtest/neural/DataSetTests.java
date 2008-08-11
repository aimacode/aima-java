package aima.test.learningtest.neural;

import java.util.List;

import junit.framework.TestCase;
import aima.learning.neural.NNExample;
import aima.learning.neural.RabbitEyeDataSet;

;

public class DataSetTests extends TestCase {

	public void testNormalizationOfFileBasedDataProducesCorrectMeanStdDevAndNormalizedValues()
			throws Exception {
		RabbitEyeDataSet reds = new RabbitEyeDataSet();
		reds.createNormalizedDataFromFile("rabbiteyes");

		List<Double> means = reds.getMeans();
		assertEquals(2, means.size());
		assertEquals(244.771, means.get(0), 0.001);
		assertEquals(145.505, means.get(1), 0.001);

		List<Double> stdev = reds.getStdevs();
		assertEquals(2, stdev.size());
		assertEquals(213.554, stdev.get(0), 0.001);
		assertEquals(65.776, stdev.get(1), 0.001);

		List<List<Double>> normalized = reds.getNormalizedData();
		assertEquals(70, normalized.size());

		// check first value
		assertEquals(-1.0759, normalized.get(0).get(0), 0.001);
		assertEquals(-1.882, normalized.get(0).get(1), 0.001);

		// check last Value
		assertEquals(2.880, normalized.get(69).get(0), 0.001);
		assertEquals(1.538, normalized.get(69).get(1), 0.001);
	}

	public void testExampleFormation() throws Exception {
		RabbitEyeDataSet reds = new RabbitEyeDataSet();
		reds.createExamplesFromFile("rabbiteyes");
		assertEquals(70, reds.howManyExamplesLeft());
		NNExample e = reds.getExampleAtRandom();
		assertEquals(69, reds.howManyExamplesLeft());
		NNExample e2 = reds.getExampleAtRandom();
		assertEquals(68, reds.howManyExamplesLeft());
	}

}
