package aima.test.core.unit.learning.framework;

import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetFactory;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class InformationAndGainTest {

	@Test
	public void testInformationCalculation() {
		double[] fairCoinProbabilities = new double[] { 0.5, 0.5 };
		double[] loadedCoinProbabilities = new double[] { 0.01, 0.99 };

		Assert
				.assertEquals(1.0, Util.information(fairCoinProbabilities),
						0.001);
		Assert.assertEquals(0.08079313589591118, Util
				.information(loadedCoinProbabilities), 0.000000000000000001);
	}

	@Test
	public void testBasicDataSetInformationCalculation() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		double infoForTargetAttribute = ds.getInformationFor();// this should
		// be the
		// generic
		// distribution
		Assert.assertEquals(1.0, infoForTargetAttribute, 0.001);
	}

	@Test
	public void testDataSetSplit() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Hashtable<String, DataSet> hash = ds.splitByAttribute("patrons");// this
		// should
		// be
		// the
		// generic
		// distribution
		Assert.assertEquals(3, hash.keySet().size());
		Assert.assertEquals(6, hash.get("Full").size());
		Assert.assertEquals(2, hash.get("None").size());
		Assert.assertEquals(4, hash.get("Some").size());
	}

	@Test
	public void testGainCalculation() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		double gain = ds.calculateGainFor("patrons");
		Assert.assertEquals(0.541, gain, 0.001);
		gain = ds.calculateGainFor("type");
		Assert.assertEquals(0.0, gain, 0.001);
	}
}
