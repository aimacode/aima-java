/*
 * Created on Apr 9, 2005
 *
 */
package aima.test.learningtest;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.learning.framework.DataSetSpecification;
import aima.learning.framework.Example;
import aima.learning.neural.IrisDataSetNumerizer;
import aima.learning.neural.Numerizer;
import aima.util.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class DataSetTest extends TestCase {
	private static final String NO = "No";

	private static final String YES = "Yes";

	DataSetSpecification spec;

	public void testLoadsDatasetFile() throws Exception {

		DataSet ds = DataSetFactory.getRestaurantDataSet();
		assertEquals(12, ds.size());

		Example first = ds.getExample(0);
		assertEquals(YES, first.getAttributeValueAsString("alternate"));
		assertEquals("$$$", first.getAttributeValueAsString("price"));
		assertEquals("0-10", first.getAttributeValueAsString("wait_estimate"));
		assertEquals(YES, first.getAttributeValueAsString("will_wait"));
		assertEquals(YES, first.targetValue());
	}

	public void testThrowsExceptionForNonExistentFile()
			throws FileNotFoundException {
		try {
			DataSet ds = new DataSetFactory().fromFile("nonexistent", null,
					null);
			fail("should have thrown Exception");
		} catch (Exception ex) {

		}

	}

	public void testLoadsIrisDataSetWithNumericAndStringAttributes()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(0);
		assertEquals("5.1", first.getAttributeValueAsString("sepal_length"));
	}

	public void testNonDestructiveRemoveExample() throws Exception {
		DataSet ds1 = DataSetFactory.getRestaurantDataSet();
		DataSet ds2 = ds1.removeExample(ds1.getExample(0));
		assertEquals(12, ds1.size());
		assertEquals(11, ds2.size());
	}

	public void testNumerizesAndDeNumerizesIrisDataSetExample1()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(0);
		Numerizer n = new IrisDataSetNumerizer();
		Pair<List<Double>, List<Double>> io = n.numerize(first);

		assertEquals(Arrays.asList(5.1, 3.5, 1.4, 0.2), io.getFirst());
		assertEquals(Arrays.asList(0.0, 0.0, 1.0), io.getSecond());

		String plant_category = n.denumerize(Arrays.asList(0.0, 0.0, 1.0));
		assertEquals("setosa", plant_category);
	}

	public void testNumerizesAndDeNumerizesIrisDataSetExample2()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(51);
		Numerizer n = new IrisDataSetNumerizer();
		Pair<List<Double>, List<Double>> io = n.numerize(first);

		assertEquals(Arrays.asList(6.4, 3.2, 4.5, 1.5), io.getFirst());
		assertEquals(Arrays.asList(0.0, 1.0, 0.0), io.getSecond());

		String plant_category = n.denumerize(Arrays.asList(0.0, 1.0, 0.0));
		assertEquals("versicolor", plant_category);
	}

	public void testNumerizesAndDeNumerizesIrisDataSetExample3()
			throws Exception {
		DataSet ds = DataSetFactory.getIrisDataSet();
		Example first = ds.getExample(100);
		Numerizer n = new IrisDataSetNumerizer();
		Pair<List<Double>, List<Double>> io = n.numerize(first);

		assertEquals(Arrays.asList(6.3, 3.3, 6.0, 2.5), io.getFirst());
		assertEquals(Arrays.asList(1.0, 0.0, 0.0), io.getSecond());

		String plant_category = n.denumerize(Arrays.asList(1.0, 0.0, 0.0));
		assertEquals("virginica", plant_category);
	}

}
