package aima.core.learning.framework;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class DataSet {
	protected DataSet() {

	}

	public List<Example> examples;

	public DataSetSpecification specification;

	public DataSet(DataSetSpecification spec) {
		examples = new LinkedList<Example>();
		this.specification = spec;
	}

	public void add(Example e) {
		examples.add(e);
	}

	public int size() {
		return examples.size();
	}

	public Example getExample(int number) {
		return examples.get(number);
	}

	public DataSet removeExample(Example e) {
		DataSet ds = new DataSet(specification);

		// We stream the examples, filter the elements that match the given
		// example so we don't get them, then we loop over the result (not filtered elements)
		// and add them to the DataSet (ds) to return it afterwards
		examples.stream().filter(example -> {
			if (e.equals(example)) {
				return true;
			}
			return false;
		}).forEach(example -> ds.add(example));

		return ds;
	}

	public double getInformationFor() {
		String attributeName = specification.getTarget();
		Hashtable<String, Integer> counts = new Hashtable<String, Integer>();

		examples.stream().forEach(example -> {
			String val = example.getAttributeValueAsString(attributeName);
			if (counts.containsKey(val)) {
				counts.put(val, counts.get(val) + 1);
			} else {
				counts.put(val, 1);
			}
		});

		// Consider avoiding primitive data types, use wrappers instead to allow the usage
		// of useful JDK8 features
		double[] data = new double[counts.keySet().size()];
		Iterator<Integer> iterator = counts.values().iterator();
		for (int i = 0; i < data.length; i++) {
			data[i] = iterator.next();
		}
		data = Util.normalize(data);

		return Util.information(data);
	}

	public Hashtable<String, DataSet> splitByAttribute(String attributeName) {
		Hashtable<String, DataSet> results = new Hashtable<String, DataSet>();

		examples.stream().forEach(example -> {
			String val = example.getAttributeValueAsString(attributeName);
			if (results.containsKey(val)) {
				results.get(val).add(example);
			} else {
				DataSet ds = new DataSet(specification);
				ds.add(example);
				results.put(val, ds);
			}
		});

		return results;
	}

	public double calculateGainFor(String parameterName) {
		Hashtable<String, DataSet> hash = splitByAttribute(parameterName);
		double totalSize = examples.size();

		final AtomicReference<Double> remainder = new AtomicReference<>();
		remainder.set(0.0);

		hash.keySet().stream()
				     .forEach(parameterValue -> {
						 double reducedDataSetSize = hash.get(parameterValue).examples.size();
						 remainder.set(remainder.get() + ((reducedDataSetSize / totalSize)
								 * hash.get(parameterValue).getInformationFor()));
				     });

		return getInformationFor() - remainder.get();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		DataSet other = (DataSet) o;
		return examples.equals(other.examples);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	public Iterator<Example> iterator() {
		return examples.iterator();
	}

	public DataSet copy() {
		DataSet ds = new DataSet(specification);

		// We stream the examples, and loop over it's elements to add
		// them to the DataSet (ds)
		examples.stream().forEach(example -> ds.add(example));

		return ds;
	}

	public List<String> getAttributeNames() {
		return specification.getAttributeNames();
	}

	public String getTargetAttributeName() {
		return specification.getTarget();
	}

	public DataSet emptyDataSet() {
		return new DataSet(specification);
	}

	/**
	 * @param specification
	 *            The specification to set. USE SPARINGLY for testing etc ..
	 *            makes no semantic sense
	 */
	public void setSpecification(DataSetSpecification specification) {
		this.specification = specification;
	}

	public List<String> getPossibleAttributeValues(String attributeName) {
		return specification.getPossibleAttributeValues(attributeName);
	}

	public DataSet matchingDataSet(String attributeName, String attributeValue) {
		DataSet ds = new DataSet(specification);

		// We stream the examples, don't filter the elements that match the given
		// attributeName and attributeValue so we get them, then we loop over the result (not filtered elements)
		// and add them to the DataSet (ds) to return it afterwards
		examples.stream().filter(example -> {
			if (example.getAttributeValueAsString(attributeName).equals(attributeValue)) {
				return false;
			}
			return true;
		}).forEach(example -> ds.add(example));

		return ds;
	}

	public List<String> getNonTargetAttributes() {
		return Util.removeFrom(getAttributeNames(), getTargetAttributeName());
	}
}
