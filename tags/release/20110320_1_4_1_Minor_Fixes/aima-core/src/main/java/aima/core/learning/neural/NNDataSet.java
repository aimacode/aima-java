package aima.core.learning.neural;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.learning.data.DataResource;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.util.Util;
import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class NNDataSet {
	/*
	 * This class represents a source of examples to the rest of the nn
	 * framework. Assumes only one function approximator works on an instance at
	 * a given point in time
	 */
	/*
	 * the parsed and preprocessed form of the dataset.
	 */
	private List<NNExample> dataset;
	/*
	 * a copy from which examples are drawn.
	 */
	private List<NNExample> presentlyProcessed = new ArrayList<NNExample>();;

	/*
	 * list of mean Values for all components of raw data set
	 */
	private List<Double> means;

	/*
	 * list of stdev Values for all components of raw data set
	 */
	private List<Double> stdevs;
	/*
	 * the normalized data set
	 */
	protected List<List<Double>> nds;

	/*
	 * the column numbers of the "target"
	 */

	protected List<Integer> targetColumnNumbers;

	/*
	 * population delegated to subclass because only subclass knows which
	 * column(s) is target
	 */
	public abstract void setTargetColumns();

	/*
	 * create a normalized data "table" from the data in the file. At this
	 * stage, the data isnot split into input pattern and tragets
	 */
	public void createNormalizedDataFromFile(String filename) throws Exception {

		List<List<Double>> rds = new ArrayList<List<Double>>();

		// create raw data set
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(DataResource.class.getResourceAsStream(
						filename + ".csv")));
		String line;
		while ((line = reader.readLine()) != null) {
			rds.add(exampleFromString(line, ","));
		}

		// normalize raw dataset
		nds = normalize(rds);
	}

	/*
	 * create a normalized data "table" from the DataSet using numerizer. At
	 * this stage, the data isnot split into input pattern and targets TODO
	 * remove redundancy of recreating the target columns. the numerizer has
	 * already isolated the targets
	 */
	public void createNormalizedDataFromDataSet(DataSet ds, Numerizer numerizer)
			throws Exception {

		List<List<Double>> rds = rawExamplesFromDataSet(ds, numerizer);
		// normalize raw dataset
		nds = normalize(rds);
	}

	/*
	 * Gets (and removes) a random example from the 'presentlyProcessed'
	 */
	public NNExample getExampleAtRandom() {

		int i = Util.randomNumberBetween(0, (presentlyProcessed.size() - 1));
		return presentlyProcessed.remove(i);
	}

	/*
	 * Gets (and removes) a random example from the 'presentlyProcessed'
	 */
	public NNExample getExample(int index) {

		return presentlyProcessed.remove(index);
	}

	/*
	 * check if any more examples remain to be processed
	 */
	public boolean hasMoreExamples() {
		return presentlyProcessed.size() > 0;
	}

	/*
	 * check how many examples remain to be processed
	 */
	public int howManyExamplesLeft() {
		return presentlyProcessed.size();
	}

	/*
	 * refreshes the presentlyProcessed dataset so it can be used for a new
	 * epoch of training.
	 */
	public void refreshDataset() {
		presentlyProcessed = new ArrayList<NNExample>();
		for (NNExample e : dataset) {
			presentlyProcessed.add(e.copyExample());
		}
	}

	/*
	 * method called by clients to set up data set and make it ready for
	 * processing
	 */
	public void createExamplesFromFile(String filename) throws Exception {
		createNormalizedDataFromFile(filename);
		setTargetColumns();
		createExamples();

	}

	/*
	 * method called by clients to set up data set and make it ready for
	 * processing
	 */
	public void createExamplesFromDataSet(DataSet ds, Numerizer numerizer)
			throws Exception {
		createNormalizedDataFromDataSet(ds, numerizer);
		setTargetColumns();
		createExamples();

	}

	public List<List<Double>> getNormalizedData() {
		return nds;
	}

	public List<Double> getMeans() {
		return means;
	}

	public List<Double> getStdevs() {
		return stdevs;
	}

	//
	// PRIVATE METHODS
	//

	/*
	 * create Example instances from a normalized data "table".
	 */
	private void createExamples() {
		dataset = new ArrayList<NNExample>();
		for (List<Double> dataLine : nds) {
			List<Double> input = new ArrayList<Double>();
			List<Double> target = new ArrayList<Double>();
			for (int i = 0; i < dataLine.size(); i++) {
				if (targetColumnNumbers.contains(i)) {
					target.add(dataLine.get(i));
				} else {
					input.add(dataLine.get(i));
				}
			}
			dataset.add(new NNExample(input, target));
		}
		refreshDataset();// to populate the preentlyProcessed dataset
	}

	private List<List<Double>> normalize(List<List<Double>> rds) {
		int rawDataLength = rds.get(0).size();
		List<List<Double>> nds = new ArrayList<List<Double>>();

		means = new ArrayList<Double>();
		stdevs = new ArrayList<Double>();

		List<List<Double>> normalizedColumns = new ArrayList<List<Double>>();
		// clculate means for each coponent of example data
		for (int i = 0; i < rawDataLength; i++) {
			List<Double> columnValues = new ArrayList<Double>();
			for (List<Double> rawDatum : rds) {
				columnValues.add(rawDatum.get(i));
			}
			double mean = Util.calculateMean(columnValues);
			means.add(mean);

			double stdev = Util.calculateStDev(columnValues, mean);
			stdevs.add(stdev);

			normalizedColumns.add(Util.normalizeFromMeanAndStdev(columnValues,
					mean, stdev));

		}
		// re arrange data from columns
		// TODO Assert normalized columns have same size etc

		int columnLength = normalizedColumns.get(0).size();
		int numberOfColumns = normalizedColumns.size();
		for (int i = 0; i < columnLength; i++) {
			List<Double> lst = new ArrayList<Double>();
			for (int j = 0; j < numberOfColumns; j++) {
				lst.add(normalizedColumns.get(j).get(i));
			}
			nds.add(lst);
		}
		return nds;
	}

	private List<Double> exampleFromString(String line, String separator) {
		// assumes all values for inout and target are doubles
		List<Double> rexample = new ArrayList<Double>();
		List<String> attributeValues = Arrays.asList(line.split(separator));
		for (String valString : attributeValues) {
			rexample.add(Double.parseDouble(valString));
		}
		return rexample;
	}

	private List<List<Double>> rawExamplesFromDataSet(DataSet ds,
			Numerizer numerizer) {
		// assumes all values for inout and target are doubles
		List<List<Double>> rds = new ArrayList<List<Double>>();
		for (int i = 0; i < ds.size(); i++) {
			List<Double> rexample = new ArrayList<Double>();
			Example e = ds.getExample(i);
			Pair<List<Double>, List<Double>> p = numerizer.numerize(e);
			List<Double> attributes = p.getFirst();
			for (Double d : attributes) {
				rexample.add(d);
			}
			List<Double> targets = p.getSecond();
			for (Double d : targets) {
				rexample.add(d);
			}
			rds.add(rexample);
		}
		return rds;
	}
}
