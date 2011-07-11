package aima.test.core.unit.learning.inductive;

import java.util.List;

import aima.core.learning.framework.DataSet;
import aima.core.learning.inductive.DLTest;
import aima.core.learning.inductive.DLTestFactory;

/**
 * @author Ravi Mohan
 * 
 */
public class MockDLTestFactory extends DLTestFactory {

	private List<DLTest> tests;

	public MockDLTestFactory(List<DLTest> tests) {
		this.tests = tests;
	}

	@Override
	public List<DLTest> createDLTestsWithAttributeCount(DataSet ds, int i) {
		return tests;
	}
}
