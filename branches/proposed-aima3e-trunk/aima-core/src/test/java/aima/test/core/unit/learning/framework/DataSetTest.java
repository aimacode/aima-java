package aima.test.core.unit.learning.framework;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.Example;
import aima.core.learning.neural.IrisDataSetNumerizer;
import aima.core.learning.neural.Numerizer;
import aima.core.learning.neural.RabbitEyeDataSet;
import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class DataSetTest {
	private static final String YES = "Yes";

	DataSetSpecification spec;

	@Test
	public void testNormalizationOfFileBasedDataProducesCorrectMeanStdDevAndNormalizedValues()
			throws Exception {
		RabbitEyeDataSet reds = new RabbitEyeDataSet();
		reds.createNormalizedDataFromFile("rabbiteyes");

		List<Double> means = reds.getMeans();
		Assert.assertEquals(2, means.size());
		Assert.assertEquals(244.771, means.get(0), 0.001);
		Assert.assertEquals(145.505, means.get(1), 0.001);

		List<Double> stdev = reds.getStdevs();
		Assert.assertEquals(2, stdev.size());
		Assert.assertEquals(213.554, stdev.get(0), 0.001);
		Assert.assertEquals(65.776, stdev.get(1), 0.001);

		List<List<Double>> normalized = reds.getNormalizedData();
		Assert.assertEquals(70, normalized.size());

		// check first value
		Assert.assertEquals(-1.0759, normalized.get(0).get(0), 0.001);
		Assert.assertEquals(-1.882, normalized.get(0).get(1), 0.001);

		// check last Value
		Assert.assertEquals(2.880, normalized.get(69).get(0), 0.001);
		Assert.assertEquals(1.538, normalized.get(69).get(1), 0.001);
	}

	@Test
	public void testExampleFormation() throws Exception {
		RabbitEyeDataSet reds = new RabbitEyeDataSet();
		reds.createExamplesFromFile("rabbiteyes");
		Assert.assertEquals(70, reds.howManyExamplesLeft());
		reds.getExampleAtRandom();
		Assert.assertEquals(69, reds.howManyExamplesLeft());
		reds.getExampleAtRandom();
		Assert.assertEquals(68, reds.howManyExamplesLeft());
	}

	@Test
	public void testLoadsDatasetFile() throws Exception {

		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Assert.assertEquals(12, ds.size());

		Example first = ds.getExample(0);
		Assert.assertEquals(YES, first.getAttributeValueAsString("alternate"));
		Assert.assertEquals("$$$", first.getAttributeValueAsString("price"));
		Assert.assertEquals("0-10", first
				.getAttributeValueAsString("wait_estimate"));
		Assert.assertEquals(YES, first.getAttributeValueAsString("will_wait"));
		Assert.assertEquals(YES, first.targetValue());
	}

	@Test(expected = Exception.class)
	public void testThrowsExceptionForNonExistentFile() throws Exception {
		new DataSetFactory().fromFile("nonexistent", null, null);
	}

	@Test
	public void testLoadsIrisDataSetWithNumericAndStringAttributes()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(0);
		Assert.assertEquals("5.1", first
				.getAttributeValueAsString("sepal_length"));
	}

	@Test
	public void testNonDestructiveRemoveExample() throws Exception {
		DataSet ds1 = DataSetFactory.getRestaurantDataSet();
		DataSet ds2 = ds1.removeExample(ds1.getExample(0));
		Assert.assertEquals(12, ds1.size());
		Assert.assertEquals(11, ds2.size());
	}

	@Test
	public void testNumerizesAndDeNumerizesIrisDataSetExample1()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(0);
		Numerizer n = new IrisDataSetNumerizer();
		Pair<List<Double>, List<Double>> io = n.numerize(first);

		Assert.assertEquals(Arrays.asList(5.1, 3.5, 1.4, 0.2), io.getFirst());
		Assert.assertEquals(Arrays.asList(0.0, 0.0, 1.0), io.getSecond());

		String plant_category = n.denumerize(Arrays.asList(0.0, 0.0, 1.0));
		Assert.assertEquals("setosa", plant_category);
	}

	@Test
	public void testNumerizesAndDeNumerizesIrisDataSetExample2()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(51);
		Numerizer n = new IrisDataSetNumerizer();
		Pair<List<Double>, List<Double>> io = n.numerize(first);

		Assert.assertEquals(Arrays.asList(6.4, 3.2, 4.5, 1.5), io.getFirst());
		Assert.assertEquals(Arrays.asList(0.0, 1.0, 0.0), io.getSecond());

		String plant_category = n.denumerize(Arrays.asList(0.0, 1.0, 0.0));
		Assert.assertEquals("versicolor", plant_category);
	}

	@Test
	public void testNumerizesAndDeNumerizesIrisDataSetExample3()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(100);
		Numerizer n = new IrisDataSetNumerizer();
		Pair<List<Double>, List<Double>> io = n.numerize(first);

		Assert.assertEquals(Arrays.asList(6.3, 3.3, 6.0, 2.5), io.getFirst());
		Assert.assertEquals(Arrays.asList(1.0, 0.0, 0.0), io.getSecond());

		String plant_category = n.denumerize(Arrays.asList(1.0, 0.0, 0.0));
		Assert.assertEquals("virginica", plant_category);
	}
}
