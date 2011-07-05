package aima.test.core.unit.learning.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.learning.framework.DataSetSpecification;

/**
 * @author Ravi Mohan
 * 
 */
public class MockDataSetSpecification extends DataSetSpecification {

	public MockDataSetSpecification(String targetAttributeName) {
		setTarget(targetAttributeName);
	}

	@Override
	public List<String> getAttributeNames() {
		return new ArrayList<String>();
	}
}
