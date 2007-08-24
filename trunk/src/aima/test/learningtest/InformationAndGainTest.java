/*
 * Created on Jul 25, 2005
 *
 */
package aima.test.learningtest;

import java.util.Hashtable;

import junit.framework.TestCase;
import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.util.Util;

/**
 * @author Ravi Mohan
 * 
 */

public class InformationAndGainTest extends TestCase {
	public void testInformationCalculation() {
		double[] fairCoinProbabilities = new double[] { 0.5, 0.5 };
		double[] loadedCoinProbabilities = new double[] { 0.01, 0.99 };

		assertEquals(1.0, Util.information(fairCoinProbabilities));
		assertEquals(0.08079313589591118, Util
				.information(loadedCoinProbabilities));
	}

	public void testBasicDataSetInformationCalculation() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		double infoForTargetAttribute = ds.getInformationFor();// this should
		// be the
		// generic
		// distribution
		assertEquals(1.0, infoForTargetAttribute);
	}

	public void testDataSetSplit() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Hashtable<String, DataSet> hash = ds.splitByAttribute("patrons");// this
		// should
		// be
		// the
		// generic
		// distribution
		assertEquals(3, hash.keySet().size());
		assertEquals(6, hash.get("Full").size());
		assertEquals(2, hash.get("None").size());
		assertEquals(4, hash.get("Some").size());

	}

	public void testGainCalculation() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Hashtable<String, DataSet> hash = ds.splitByAttribute("patrons");
		double gain = ds.calculateGainFor("patrons");
		assertEquals(0.541, gain, 0.001);
		gain = ds.calculateGainFor("type");
		assertEquals(0.0, gain, 0.001);
	}

}
